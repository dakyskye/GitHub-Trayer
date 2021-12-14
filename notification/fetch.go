package notification

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"
)

func (n *Notification) Fetch() error {
	req, err := http.NewRequest("GET", "https://api.github.com/notifications", nil)
	if err != nil {
		return fmt.Errorf("unable to generate a GET request to GitHub notifications API: %w", err)
	}

	req.Header.Set("Authorization", "token "+n.token)

	client := &http.Client{}

	res, err := client.Do(req)
	if err != nil {
		return fmt.Errorf("unable to make a GET request to GitHub notifications API: %w", err)
	}

	body, err := io.ReadAll(res.Body)
	if err != nil {
		return fmt.Errorf("unable to read the response from GitHub notifications API: %w", err)
	}
	_ = res.Body.Close()

	var dyn []interface{}

	err = json.Unmarshal(body, &dyn)
	if err != nil {
		return fmt.Errorf("unable to parse the response from GitHub notifications API: %w", err)
	}

	n.prevCount = n.count
	n.count = len(dyn)

	return nil
}
