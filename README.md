# GitHub Trayer

A tray application to watch your GitHub notifications.

## Why

On Linux we can add GitHub emoji to our statusbar ([like done there](https://github.com/dakyskye/dotfiles))
which would notify us when we get new GitHub notifications but on Windows it's not that easy. So I decided
to make a tray-only application that would constantly be running and taking care of notifying me of my
GitHub notifications.

## Running

Currently, I use IntelliJ IDEA's tools to build and run this project since I don't know much about compiling
Java code into JAR/Executables via commandline but later I will better document and even release a binary.

## Dependencies

This project depends on [GitHub CLI](https://cli.github.com/) to keep things simple for now, so you need
to have authenticated with *GitHub CLI* as well as have it added to your `PATH` environment variable for
this code to run properly since `gh` command[is called ](https://github.com/dakyskye/GitHub-Trayer/blob/master/src/main/java/com/github/dakyskye/github_trayer/App.java#L50)
each time to pull notifications with GitHub API for your account.

For later, I've considered adding command line interface to this program, so you'll be able to provide
your OAuth token easily and the program will make use of GitHub's web API directly; however using
*GitHub CLI* will still be an option.

## Screenshots

GitHub Trayer's tray icon automatically chooses either dark or light version of GitHub logo depending on
your OS theme settings and it automatically updates the icon after OS theme is changed as well.

![example](./screenshot.png)