let pager = {
    currentPageNumber: 1,
    totalPageCount: 0,
    totalResultCount: 0,
    itemsOnPage: 5,
    url: "api/user",
    nameVar1: "currentPage",
    nameVar2: "itemsOnPage",

    // метод инициализации полей класса
    // data - JSON array from back-end
    // url - это часть урла до знака ? .
    // Пример http://locaalhost:8080/api/user?currentPage=2&itemsOnPage=10
    // в url мы передаём строку 'api/user' или 'http://locaalhost:8080/api/user'.
    // Var1 - это имя переменной в url. Из примера выше это currentPage.
    // Var2 - это имя переменной в url. Из примера выше это itemsOnPage.
    setData: function (data, url, Var1, Var2) {
        this.currentPageNumber = data.currentPageNumber;
        this.totalPageCount = data.totalPageCount;
        this.totalResultCount = data.totalResultCount;
        this.itemsOnPage = data.itemsOnPage;
        this.url = url;
        this.nameVar1 = Var1;
        this.nameVar2 = Var2;
    },

    // Это вспомогательный метод который вызывается внутри метода getPageView().
    // Генерирует активную ссылку
    getActiveLink: function (_currentPageNum, _itemsOnPage, name) {
        return '<li class="page-item"><a class="page-link" href="' + this.url + '?' + this.nameVar1 + '=' + _currentPageNum + '&' + this.nameVar2 + '=' + _itemsOnPage + '">' + name + '</a></li>';
    },

    // Это вспомогательный метод который вызывается внутри метода getPageView().
    // Генерирует неактивную ссылку
    getDisableLink: function (name) {
        return '<li class="page-item disabled"><a class="page-link bg-primary text-white" href="#" tabindex="-1">' + name + '</a></li>';
    },

    // Этот метод возвращает pager в виде готового html кода.
    getPagerView: function () {
        let ret = '<nav aria-label="Page navigation example">';
        ret += '<ul class="pagination pagination-sm justify-content-center">';

        // Если текущая страница больше 1, то рисуем ссылку 'Пред'.
        if (this.currentPageNumber > 1) {
            ret += this.getActiveLink(this.currentPageNumber - 1, this.itemsOnPage, 'Пред');
        }

        // Если страниц не больше десяти, то рисуем пэйджер без точек ...
        // т.е. пэйджер будет не большой.
        // Пример - 1 2 3 4 5 6 7 8 9 10
        if (this.totalPageCount <= 10) {
            for (let i = 1; i <= this.totalPageCount; i++) {
                if (i != this.currentPageNumber) {
                    ret += this.getActiveLink(i, this.itemsOnPage, i);
                } else {
                    ret += this.getDisableLink(i);
                }
            }
        // Если страниц больше десяти, то рисуем пэйджер с точками ...
        // Пример - 1 ... 22 23 24 25 26 ... 101
        } else {
            // Если текущая страница меньше 5, то рисуем
            // Пример - 1 2 3 4 5
            if (this.currentPageNumber < 5) {
                for (let i = 1; i <= 5; i++) {
                    if (i != this.currentPageNumber) {
                        ret += this.getActiveLink(i, this.itemsOnPage, i);
                    } else {
                        ret += this.getDisableLink(i);
                    }
                }
                // тут добавляются точки ...
                ret += '&nbsp;...&nbsp;';
                // тут добавляется последняя страница в пейджер например 23
                ret += this.getActiveLink(this.totalPageCount, this.itemsOnPage, this.totalPageCount);
                // Итоговый пейджер получится - 1 2 3 4 5 ... 23

            // Если текущая страница больше или равна 5 или меньше 4 последним (totalPageCount - 4) , то добавляем первую страницу.
            // Потом рисуем точки ... . Потом вычисляем начальное число и конечное от текущей страницы для цикла.
            // Например текуща страница 7, тогда конечное будет 7+2=9, а начальное 9-4=5, после этого добавляем
            // в цикле 5 ссылок включая текущую. Потом Добавляем точки ... и последнюю страницу.
            } else if (this.currentPageNumber >= 5 && this.currentPageNumber <= this.totalPageCount - 4) {
                // Первая страница
                ret += this.getActiveLink(1, this.itemsOnPage, 1);
                // Точки ...
                ret += '&nbsp;...&nbsp;';
                // вычисляем начальное число и конечное от текущей страницы для цикла.
                // Например текуща страница 7, тогда конечное будет 7+2=9, а начальное 9-4=5, после этого добавляем
                // в цикле 5 ссылок включая текущую.
                let end = this.currentPageNumber + 2;
                let start = end - 4;
                for (let i = start; i <= end; i++) {
                    if (i != this.currentPageNumber) {
                        ret += this.getActiveLink(i, this.itemsOnPage, i);
                    } else {
                        ret += this.getDisableLink(i);
                    }
                }
                // Точки ...
                ret += '&nbsp;...&nbsp;';
                // Последняя страница например 23
                ret += this.getActiveLink(this.totalPageCount, this.itemsOnPage, this.totalPageCount);
                // В итоге должно получиться - 1 ... 5 6 7 8 9 ... 23

            } else {
                // Сюда попадём если текущая страница больше (totalPageCount - 4)
                // Добавояем  первую страницу
                ret += this.getActiveLink(1, this.itemsOnPage, 1);
                // Добавляем точки ...
                ret += '&nbsp;...&nbsp;';
                // Добавляем ссылки 5 от конца пэйджера.
                for (let i = this.totalPageCount - 4; i <= this.totalPageCount; i++) {
                    if (i != this.currentPageNumber) {
                        ret += this.getActiveLink(i, this.itemsOnPage, i);
                    } else {
                        ret += this.getDisableLink(i);
                    }
                }
                // В итоге должно получится - 1 ... 19 20 21 22 23

            }
        }

        // Если текущая страница меньше последней, то добавляем ссылку 'След'.
        if (this.currentPageNumber < this.totalPageCount) {
            ret += this.getActiveLink(this.currentPageNumber + 1, this.itemsOnPage, 'След');
        }

        ret += '</ul>';
        ret += '</nav>';

        return ret;
    }
}