function ppager() {

    fetch("/api/auth/token", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({'login': 'user1@user.com', 'pass': 'user1'})
    }).then(response => {

        if (response.status === 200) {
            response.json().then(data => {
                var TOKEN = data.token;
                fetch('/api/user?currentPage=2&itemsOnPage=1', {
                    method: "GET",
                    headers: {
                        'Authorization': 'Bearer ' + TOKEN
                    }
                })
                    .then((response) => {
                        //console.log(response);
                        return response.json();
                    })
                    .then((data) => {
                        console.log(data);
                        pager.setData(data, "api/user","page", "items");
                        document.getElementById("pager").insertAdjacentHTML("afterend", pager.getPagerView());
                    });

            });
        }
    });
}