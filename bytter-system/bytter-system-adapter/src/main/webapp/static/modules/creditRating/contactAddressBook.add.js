bt.use(['form', 'layedit','dicts', 'selectTree','rateType','selectType','moneyType','dateType'], function () {
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();
    select = bt.selectType.init();
    moneyType = bt.moneyType.init();
    rateTypes = bt.rateType.init();//利率
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();//获取时间操作方法

    // var currentData = bt.navtab.getParentDom().currentRowByContact;
    // if(currentData){
    //     for (var key in currentData){
    //         $('[name="+key+"]').val(currentData[key])
    //     }
    // }

    form.verify({
        emailFex: function (value) {
            var vals = value.split(";");
            for (var i = 0; i < vals.length; i++) {
                if (vals[i] && !/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(vals[i])) {
                    return bt.lang('sys.form.verify.emailError')//'邮箱格式不正确';
                }
            }
        }
    });
    //监听保存
    form.on('submit', function(data){
        // bt.navtab.getParentDom().addContactInfo(data.field);
        // bt.navtab.closeCurrent();
        bt.loading.show();
        var action="/credit/contactAddressBook/saveDetail";
        $.post(basePath + action, data.field, function (result) {
            // debugger;
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                var webObj = bt.navtab.getParentDom();

                if($("#update").val()=="0") {
                    var bookIds = webObj.bt.jquery("#bookIds").val();
                    //给父级选项卡页面中id为test的input赋值;
                    if (webObj.bt.jquery("#bookIds").val() == "") {
                        bookIds = result.obj
                    } else {
                        bookIds = bookIds + "," + result.obj
                    }
                    webObj.bookBttable.reload({url:basePath + "/credit/contactAddressBook/detailDatatable?ids="+bookIds});
                    webObj.bt.jquery("#bookIds").val(bookIds);
                }else {
                    webObj.bookBttable.reload();
                }
                bt.navtab.closeCurrent();
            }
            bt.loading.hide();
        }, 'JSON');
        return false;
    });
})


