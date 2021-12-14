package main

import (
	"github.com/dakyskye/github-trayer/assets"
	"github.com/dakyskye/github-trayer/notification"
	"github.com/getlantern/systray"
	"log"
	"os"
	"time"
)

const title = "GitHub Trayer"

func main() {
	if len(os.Args) == 1 {
		log.Println("please provide your token as an argument")
		os.Exit(1)
	}
	systray.Run(onReady, nil)
}

func onReady() {
	systray.SetTemplateIcon(assets.IconDark, assets.IconDark)
	systray.SetIcon(assets.IconDark)
	systray.SetTitle(title)
	systray.SetTooltip(title)

	notif := notification.New(
		title,
		notification.NotificationIconPath{
			Light: "assets/github_mark_plus_light.png",
			Dark:  "assets/github_mark_plus.png",
		},
		time.Second*12,
		os.Args[1],
	)

	notifChan := notif.Subscribe()
	refreshChan := systray.AddMenuItem("Refresh", "Refresh notifications count").ClickedCh
	quitChan := systray.AddMenuItem("Quit", "Quit "+title).ClickedCh

	for {
		select {
		case <-notifChan:
			err := notif.NotifyIfChanged(true)
			if err != nil {
				log.Println("failed to send a notification", err)
			}
		case <-refreshChan:
			err := notif.Fetch()
			if err != nil {
				log.Println("failed to fetch notifications", err)
				continue
			}

			err = notif.Notify(true)
			if err != nil {
				log.Println("failed to send a notification", err)
			}
		case <-quitChan:
			systray.Quit()
		}
	}
}
