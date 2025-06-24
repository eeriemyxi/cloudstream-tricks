# CloudStream Tricks
CloudStream Tricks is a personal solution which lets you open CloudStream streams on
your PC's local media player.

It has been tested only on Linux so far, but I don't see any reason why it
couldn't be adapted for other platforms, but I don't plan to provide support for
other platforms just yet.

This project maintains an Android APK and a web server that is powered by
[Odin](https://odin-lang.org/).

# Demo
https://github.com/user-attachments/assets/d35e74e7-19ee-4788-a5b1-3a7afa3923b5

# Installation Guide
As of now you'll have to build the APK and the web server yourself.

To clone the repository properly, do:
```
git clone --recursive --depth 1 https://github.com/eeriemyxi/cloudstream-tricks
```

> [!NOTE]
> Omit `--depth 1` if you aim to contribute to the project.

To build the web server, install Odin and head over to `ct-daemon` directory. Then execute:
```
odin build .
```

Now you should have a `ct-daemon` executable in the folder.

To build the Android app, please follow some instructions on doing it on Android
Studio on the internet.

# Usage Guide
The usage guide is simple. You run the `ct-daemon` web server on your PC. It'll
tell you the address it is running the web server at. At some point I'm going to
make it customizable, but for now it is accepting any IP4 address with the port
`8000`.

> [!WARNING]
> The web server assumes you'll be using `mpv`.
> This should be customizable at some point at a higher level,
> but for now you'd have to edit the source code to change it.

Open the "CloudStream Tricks" Android app, then save the address you saw there.

There is a load I can tell you here, but to keep it simple, in the hostname
field of the app, you would ideally enter the IPV6 address of your PC from `ip
a` command. You would also want to connect to the same WIFI that your PC is
connected to in this case.

Now open CloudStream, pick a stream, tap and hold it, press "Play in m3u8
player", then select CloudStream Tricks. You should see a toast message quickly.

> [!TIP]
> You can make m3u8 the default in the settings of CloudStream (under Player settings).         
