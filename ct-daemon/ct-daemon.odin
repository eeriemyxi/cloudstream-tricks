package ct_daemon

import "core:fmt"
import "core:log"
import "core:net"
import os "core:os/os2"
import http "./odin-http"

main :: proc() {
    context.logger = log.create_console_logger(.Info)

    s: http.Server
    http.server_shutdown_on_interrupt(&s)

    router: http.Router
    http.router_init(&router)
    defer http.router_destroy(&router)

    http.route_post(&router, "/play", http.handler(play))

    routed := http.router_handler(&router)

    log.info("Listening on http://0.0.0.0:8000")

    err := http.listen_and_serve(&s, routed, net.Endpoint{address = net.IP4_Any, port = 8000})
    fmt.assertf(err == nil, "server stopped with error: %v", err)
}

play :: proc(req: ^http.Request, res: ^http.Response) {
    http.body(req, -1, res, proc(res: rawptr, body: http.Body, err: http.Body_Error) {
        res := cast(^http.Response)res

        if err != nil {
            http.respond(res, http.body_error_status(err))
            log.error(err)
            return
        }

        ma, ok := http.body_url_encoded(body)
        if !ok {
            http.respond(res, http.Status.Bad_Request)
            return
        }

        r, w, err := os.pipe()

        defer os.close(r)
        defer os.close(w)

        if err != nil {
            http.respond(res, http.Status.Unprocessable_Content)
            log.error("ERROR: Couldn't create pipe for some reason.")
            return
        }

        mpvresp, perr := os.process_start({command = {"mpv", "-"}, stdout=os.stdout, stderr=os.stderr, stdin=r})
        os.write(w, transmute([]u8)fmt.tprint(body))

        if err != nil {
            http.respond_plain(res, "MPV failed with an error. Check server logs.")
            log.error("ERROR: MPV returned error")
            return
        }

        http.respond_plain(res, "Successfully received the m3u8 file. Starting MPV on the target machine...")
    })
    return
}
