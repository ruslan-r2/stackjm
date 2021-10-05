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
            if (document.cookie.split(';').filter(function(item) {
                return item.trim().indexOf('token=') === 0
            }).length) {
                setCookie('token', '', {'max-age': -1});
            }
            setCookie('token', data.token, {'max-age': 3600, SameSite: 'strict'});
            window.location.href = "/main";
        })
    } else if (response.status === 403) {
        alert("Неверный email или пароль");
    }
}

function setCookie(name, value, options = {}) {
    options = {
        // при необходимости добавьте другие значения по умолчанию
        path: '/',
        ...options
    };

    if (options.expires instanceof Date) {
        options.expires = options.expires.toUTCString();
    }

    let updatedCookie = encodeURIComponent(name) + "=" + encodeURIComponent(value);

    for (let optionKey in options) {
        updatedCookie += "; " + optionKey;
        let optionValue = options[optionKey];
        if (optionValue !== true) {
            updatedCookie += "=" + optionValue;
        }
    }

    document.cookie = updatedCookie;
}