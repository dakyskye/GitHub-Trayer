package notification

import (
	"fmt"
	"github.com/gen2brain/beeep"
	"time"
)

type Notification struct {
	title     string
	token     string
	duration  time.Duration
	chann     chan struct{}
	count     int
	prevCount int
	icons     NotificationIconPath
}

type NotificationIconPath struct {
	Light string
	Dark  string
}

func New(title string, icons NotificationIconPath, duration time.Duration, token string) *Notification {
	return &Notification{
		title:     title,
		token:     token,
		duration:  duration,
		chann:     make(chan struct{}),
		count:     0,
		prevCount: 0,
		icons:     icons,
	}
}

func (n *Notification) NotifyIfChanged(useDarkIcon bool) error {
	if n.count == n.prevCount {
		return nil
	}

	return n.Notify(useDarkIcon)
}

func (n *Notification) Notify(useDarkIcon bool) error {
	var (
		message  string
		iconPath string
	)

	if n.count > n.prevCount {
		message = fmt.Sprintf("you have %d more unread notifications, %d in total", n.count-n.prevCount, n.count)
	} else if n.count < n.prevCount {
		message = fmt.Sprintf("you have %d less unread notifications, %d in total", n.prevCount-n.count, n.count)
	} else {
		message = fmt.Sprintf("you have %d unread notifications", n.count)
	}

	if useDarkIcon {
		iconPath = n.icons.Dark
	} else {
		iconPath = n.icons.Light
	}

	return beeep.Notify(n.title, message, iconPath)
}
