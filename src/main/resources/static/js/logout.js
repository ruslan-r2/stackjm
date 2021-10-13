function performLogout() {
    document.cookie="token=; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
    window.location.href = "/login";
}