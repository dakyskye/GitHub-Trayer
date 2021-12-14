package notification

import (
	"log"
	"sync"
	"time"
)

var subscribeOnce sync.Once

func (n *Notification) Subscribe() <-chan struct{} {
	subscribeOnce.Do(func() {
		go func() {
			fetch := func() {
				err := n.Fetch()
				if err != nil {
					log.Println("could not fetch notifications", err)
				} else {
					n.chann <- struct{}{}
				}
			}

			fetch()

			ticker := time.NewTicker(n.duration)
			for range ticker.C {
				fetch()
			}
		}()
	})

	return n.chann
}
