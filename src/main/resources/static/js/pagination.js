
let pager = {
    currentPageNumber: 1,
    totalPageCount: 0,
    totalResultCount: 0,
    itemsOnPage: 5,
    url: "api/user",
    nameVar1: "currentPage",
    nameVar2: "itemsOnPage",

    setData: function (data, url, Var1, Var2) {
        this.currentPageNumber = data.currentPageNumber;
        this.totalPageCount = data.totalPageCount;
        this.totalResultCount = data.totalResultCount;
        this.itemsOnPage = data.itemsOnPage;
        this.url = url;
        this.nameVar1 = Var1;
        this.nameVar2 = Var2;
        console.log(Var1 + Var2 + this.itemsOnPage);
    },

    getActiveLink: function (_currentPageNum, _itemsOnPage, name) {
        return '<li class="page-item"><a class="page-link" href="' + this.url + '?' + this.nameVar1 + '=' + _currentPageNum + '&' + this.nameVar2 + '=' + _itemsOnPage + '">' + name + '</a></li>';
    },

    getDisableLink: function (name) {
        return '<li class="page-item disabled"><a class="page-link bg-primary text-white" href="#" tabindex="-1">' + name + '</a></li>';
    },


    getPagerView: function () {
        let ret = '<nav aria-label="Page navigation example">';
        ret += '<ul class="pagination pagination-sm justify-content-center">';

        if (this.currentPageNumber > 1) {
            ret += this.getActiveLink(this.currentPageNumber - 1, this.itemsOnPage, 'Пред');
        }

        // Если страниц не больше десяти, то рисуем пэйджер без точек ...
        // т.е. пэйджер будет не большой.
        // Пример - Предыдущая 1 2 3 4 5 6 7 8 9 10 Следующая
        if (this.totalPageCount <= 10) {
            for (let i = 1; i <= this.totalPageCount; i++) {
                if (i != this.currentPageNumber) {
                    ret += this.getActiveLink(i, this.itemsOnPage, i);
                } else {
                    ret += this.getDisableLink(i);
                }
            }
            // Если страниц больше десяти, то рисуем пэйджер с точками ...
            // Пример - Предыдущая 1 ... 22 23 24 25 26 ... 101 Следующая
        } else {
            if (this.currentPageNumber < 5) {
                for (let i = 1; i <= 5; i++) {
                    if (i != this.currentPageNumber) {
                        ret += this.getActiveLink(i, this.itemsOnPage, i);
                    } else {
                        ret += this.getDisableLink(i);
                    }
                }
                ret += '&nbsp;...&nbsp;';
                ret += this.getActiveLink(this.totalPageCount, this.itemsOnPage, this.totalPageCount);

            } else if (this.currentPageNumber >= 5 && this.currentPageNumber <= this.totalPageCount - 4) {
                ret += this.getActiveLink(1, this.itemsOnPage, 1);
                ret += '&nbsp;...&nbsp;';
                let end = this.currentPageNumber + 2;
                let start = end - 4;
                for (let i = start; i <= end; i++) {
                    if (i != this.currentPageNumber) {
                        ret += this.getActiveLink(i, this.itemsOnPage, i);
                    } else {
                        ret += this.getDisableLink(i);
                    }
                }
                ret += '&nbsp;...&nbsp;';
                ret += this.getActiveLink(this.totalPageCount, this.itemsOnPage, this.totalPageCount);

            } else {
                ret += this.getActiveLink(1, this.itemsOnPage, 1);
                ret += '&nbsp;...&nbsp;';
                for (let i = this.totalPageCount - 4; i <= this.totalPageCount; i++) {
                    if (i != this.currentPageNumber) {
                        ret += this.getActiveLink(i, this.itemsOnPage, i);
                    } else {
                        ret += this.getDisableLink(i);
                    }
                }

            }
        }

        if (this.currentPageNumber < this.totalPageCount) {
            ret += this.getActiveLink(this.currentPageNumber + 1, this.itemsOnPage, 'След');
        }

        ret += '</ul>';
        ret += '</nav>';

        return ret;
    }
}