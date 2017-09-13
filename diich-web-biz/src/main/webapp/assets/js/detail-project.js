var projectPage = {//项目详情页
    init: function () {
        this.masterMore();
        this.call();
    },
    masterMore: function () {//代表性传承人
        var master = $('.card_main .inheritor .master');
        var oldHeight = master.height() - 50;
        var ul = master.find('ul');
        var li = ul.find('li');
        var totalItem = ul.find('.item');
        var item = li.find('.item');
        var liLen = li.length;
        var page = master.find('.page');

        var prev = master.find('.prev');
        var next = master.find('.next');
        var more = master.find('.more');

        var cur = 0;

        //初始化
        li.hide();
        li.eq(0).show();


        if (item.length <= 3) {
            more.hide();
        }
        more.find('em').html(totalItem.length);  //全部多少人

        //查看其它
        more.on('click', function () {
            ul.addClass('active').animate({'height': oldHeight + 'px'}, 100);
            $(this).hide();
            li.show();
            prev.show();
            next.show();
            page.show();
            li.find('.item:gt(0)').show();
            if (liLen < 2) {
                prev.hide();
                next.hide();
            }
        });

        //判断li的个数
        prev.hide();
        next.hide();

        ul.animate({'height': '70px'}, 100);
        page.hide();
        prev.hide();
        next.hide();

        li.find('.item:gt(2)').hide();
        if (liLen > 2) {
            for (var i = 0; i < liLen; i++) {
                page.append('<span>' + common.pad(i + 1) + '</span>')
            }
        }

        var _span = page.find('span');
        _span.eq(0).addClass('active');
        _span.on('click', function () {
            cur = $(this).index();
            $(this).addClass('active').siblings('span').removeClass('active');
            ul.stop(true).animate({'margin-left': -cur * li.outerWidth(true) + 'px'}, 300);

            if (cur == 0) {
                prev.addClass('active');
            } else {
                prev.removeClass('active');
            }
            if (cur == liLen - 1) {
                next.addClass('active');
            } else {
                next.removeClass('active');
            }

        });
        prev.addClass('active');
        //下一页
        next.on('click', function () {
            prev.removeClass('active');
            if (cur < liLen - 1) {
                cur++;
            }
            _span.eq(cur).addClass('active').siblings('span').removeClass('active');
            ul.stop(true).animate({'margin-left': -cur * li.outerWidth(true) + 'px'}, 300);
        });
        //上一页
        prev.on('click', function () {
            next.removeClass('active');
            if (cur > 0) {
                cur--;
            }
            if (cur == 0) {
                $(this).addClass('active');
            }
            _span.eq(cur).addClass('active').siblings('span').removeClass('active');
            ul.stop(true).animate({'margin-left': -cur * li.outerWidth(true) + 'px'}, 300);
        });
        if (cur == liLen - 1) {
            next.addClass('active');
        }
        if (cur == 0) {
            prev.addClass('active')
        }
    },
    call:function () {//调用详情公共部分
        Detail.topPic(); //题图
        Detail.productsTab(); //产品分页tab
        Detail.scrollFloor(); //楼层导航效果
        Detail.modal.init();
        Detail.widget.init('.detail');
        //Detail.computeBaseInfo();//基本信息左右
        Detail.judgeDom(); //判断doi和非遗中国是否存在
        Detail.code_arr(); //替换资源数据
        Detail.catgary(); //查询分类
    }
};

$(function () {
    projectPage.init();
});







