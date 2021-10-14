function CSSLoad(file) {
    var link = document.createElement("link");
    link.setAttribute("rel", "stylesheet");
    link.setAttribute("type", "text/css");
    link.setAttribute("href", "https://cdn.sstatic.net/Shared/stacks.css?v=d6e71d1b13f1");
    document.getElementsByTagName("head")[0].appendChild(link)
    var link1 = document.createElement("link");
    link1.setAttribute("rel", "stylesheet");
    link1.setAttribute("type", "text/css");
    link1.setAttribute("href", "https://cdn.sstatic.net/Sites/ru/primary.css?v=624cbd0eddf4");
    document.getElementsByTagName("head")[0].appendChild(link1)
}
function ScriptLoad() {
    var script = document.createElement("script");
    script.setAttribute("type", "text/javascript");
    script.setAttribute("src", "/js/logout.js");
    document.getElementsByTagName("head")[0].appendChild(script);
}
CSSLoad('/addStyles.css');
ScriptLoad();

document.body.insertAdjacentHTML("afterbegin", "<header class=\"top-bar js-top-bar top-bar__network\">\n" +
    "  <div class=\"wmx12 mx-auto d-flex ai-center h100\" role=\"menubar\">\n" +
    "    <div class=\"-main flex--item\">\n" +
    "      <a href=\"\" class=\"-logo js-gps-track\" data-gps-track=\"top_nav.click({is_current:true, location:1, destination:8})\">\n" +
    "<img src=\"images/logoStackJM.PNG\" width=\"150px\" height=\"50px\"/>" +
    "      </a>\n" +
    "\n" +
    "\n" +
    "\n" +
    "    </div>\n" +
    "\n" +
    "\n" +
    "    <form id=\"search\" role=\"search\" action=\"/search\" class=\"flex--item fl-grow1 searchbar px12 js-searchbar \" autocomplete=\"off\">\n" +
    "      <div class=\"ps-relative\">\n" +
    "        <input name=\"q\" type=\"text\" placeholder=\"Поиск...\" value=\"\" autocomplete=\"off\" maxlength=\"240\" class=\"s-input s-input__search js-search-field \" aria-label=\"Поиск\" aria-controls=\"top-search\" data-controller=\"s-popover\" data-action=\"focus->s-popover#show\" data-s-popover-placement=\"bottom-start\">\n" +
    "        <svg aria-hidden=\"true\" class=\"s-input-icon s-input-icon__search svg-icon iconSearch\" width=\"18\" height=\"18\" viewBox=\"0 0 18 18\"><path d=\"m18 16.5-5.14-5.18h-.35a7 7 0 10-1.19 1.19v.35L16.5 18l1.5-1.5zM12 7A5 5 0 112 7a5 5 0 0110 0z\"></path></svg>\n" +
    "        <div class=\"s-popover p0 wmx100 wmn4 sm:wmn-initial js-top-search-popover\" id=\"top-search\" role=\"menu\">\n" +
    "          <div class=\"s-popover--arrow\"></div>\n" +
    "          <div class=\"js-spinner p24 d-flex ai-center jc-center d-none\">\n" +
    "            <div class=\"s-spinner s-spinner__sm fc-orange-400\">\n" +
    "              <div class=\"v-visible-sr\">Loading…</div>\n" +
    "            </div>\n" +
    "          </div>\n" +
    "\n" +
    "          <span class=\"v-visible-sr js-screen-reader-info\"></span>\n" +
    "          <div class=\"js-ac-results overflow-y-auto hmx3 d-none\"></div>\n" +
    "\n" +
    "          <div class=\"js-search-hints\" aria-describedby=\"Tips for searching\"></div>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "    </form>\n" +
    "\n" +
    "\n" +
    "\n" +
    "    <ol class=\"overflow-x-auto ml-auto -secondary d-flex ai-center list-reset h100 user-logged-out\" role=\"presentation\">\n" +
    "      <li class=\"-ctas\">\n" +
    "        <a href=\"/profile\" class=\"login-link s-btn s-btn__filled py8 js-gps-track\" rel=\"nofollow\" data-gps-track=\"login.click\" data-ga=\"[&quot;top navigation&quot;,&quot;login button click&quot;,null,null,null]\">Мой профиль</a>\n" +
    "\n" +
    "      </li>\n" +
    "      <li>\n" +
    "        <button class=\'btn btn-outline-primary ml-1 mr-1\' onclick=\'performLogout()\'>Выход</button>\n" +
    "      </li>\n" +
    "\n" +
    "    </ol>\n" +
    "\n" +
    "  </div>\n" +
    "</header>");