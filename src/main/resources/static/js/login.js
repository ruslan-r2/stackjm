function loginSubmit(e, form) {
    e.preventDefault()
    fetch("api/auth/token", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(Object.fromEntries(new FormData(form).entries()))
    }).then(response => processResponse(response))
}

function processResponse(response) {
    if (response.status === 200) {
        response.json().then(data => {
            document.cookie = "token=" + data.token;
            window.location.href = "/main";
        })
    } else if (response.status === 403) {
        alert("Неверноый email или пароль")
    }
}