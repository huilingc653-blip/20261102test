bt.use(['form','layedit','dicts', 'selectType', 'moneyType', 'rateType','element', 'selectTable','dateType','corp', 'btUpload'], function(){
    var $ = bt.jquery,form = bt.form();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var selectType = bt.selectType.init();
    bt.rateType.init();
    bt.dicts.init();

    var up = bt.btUpload.init({
        elem: "#uploader"+swapType,
        auto: false,
        gid: ''
    });

    //单位回调函数
    bt.corpCallback = function (id) {
        //获取单位信息
        $.ajax({
            url: basePath + "/sys/corp/getSysCorp",
            method: 'POST',
            data: {id: id},
            dataType: 'json',
            success: function (data) {
                if (data.obj) {
                    if (data.obj.userAccountCode) {
                        //清空联系人信息
                        $('#corpContact1').val("");
                        $('#corpContactTel1').val("");
                        $('#corpContact2').val("");
                        $('#corpContactTel2').val("");
                        //重新加载联系人
                        corpContactInit1.get("corpContact1").reload({corpId: id});
                        corpContactInit2.get("corpContact2").reload({corpId: id});
                        //加载资金计划
                        renderBudget1();
                        renderBudget2();
                    }
                }
                form.render();
            }
        });

        //重新加载保证金账户信息
        boodAccInit1.get("boodAcc1").rebuild({
            urldata: {curId: bt.selectType.get('sellCurId1').val(), corpId: id},
            columns: [
                {data:"bankAcc",},
                {data:"accName",},
                {data:"bankName",},
                {data:"curName",}
            ]
        });
        boodAccInit2.get("boodAcc2").rebuild({
            urldata: {curId: bt.selectType.get('sellCurId2').val(), corpId: id},
            columns: [
                {data:"bankAcc",},
                {data:"accName",},
                {data:"bankName",},
                {data:"curName",}
            ]
        });
    };
    //初始化单位
    var corp = bt.corp.init();

    //保证金账户信息
    var boodAccInit = bt.selectTable.init({
        elem: "#boodAccTable"+swapType,
        url: basePath+"/foreign/fextranreq/getBoodBankAcc",
        urldata: {curId: bt.selectType.get('sellCurId'+swapType).val(), corpId: $('input[name="corpId"]').val()},
        scrollX: true,
        scrollY:"200px",
        searchInput:"#boodAcc"+swapType,
        paging:true,
        columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
        ],
        clickCallback:function(data){
            if(data){
                $('#boodAcc'+swapType).val(data.bankAcc);
                $('#boodAccId'+swapType).val(data.id);
            }else{
                $('input.text').val('');
            }
        }
    });

    //交易编码
    var tranCodeInit = bt.selectTable.init({
        elem: "#tranCodeTable1"+swapType,
        url: basePath + "/foreign/fextraninfo/list",
        searchInput: "#tranName1"+swapType,
        scrollX: true,
        scrollY: "300px",
        columns: [
            {data: "tranCode",},
            {data: "tranName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#tranName1'+swapType).val(data.tranName);
                $('#tranCode1'+swapType).val(data.tranCode);
            } else {
                $('input.text').val('');
            }
        }
    });

    //联系人
    var corpContactInit = bt.selectTable.init({
        elem: "#corpContactTable"+swapType,
        url: basePath + "/foreign/fexlinkmaninfo/listByCorpId",
        urldata: {corpId: $('input[name="corpId"]').val()},
        searchInput: "#corpContact"+swapType,
        scrollX: true,
        scrollY: "200px",
        valueFromTable: false,
        columns: [
            {data: "payerName",},
            {data: "payerTel",},
        ],
        clickCallback: function (data) {
            if (data) {
                $('#corpContact'+swapType).val(data.payerName);
                $('#corpContactTel'+swapType).val(data.payerTel);
            } else {
                $('input.text').val('');
            }
        }
    });
});
