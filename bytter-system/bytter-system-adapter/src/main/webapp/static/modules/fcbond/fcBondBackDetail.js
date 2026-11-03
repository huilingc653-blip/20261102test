var datatable,dateFun,moneyType,moneyFun;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts','selectTable'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    dateFun = bt.dateType.getFun();
    bt.dateType.init();
    bt.bank.init({
        elem:'.bank',
    });

    //导出表格
    form.on('submit(excel)', function(data) {
        var url = basePath + "/bond/fcbondback/excelBondback";
        DownLoadFile({url:url,data:data.field,method:'post'});
    });

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        order: [[3, 'desc']],
        pageSizeOptions:[10, 25, 50, 100, 500],
        ignoreButtons:['colvis','print','output'],// 忽略的表单默认按钮 state取消状态保存按钮
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bond/fcbondback/fcBondApplyDatatable"
        },
        columns:[
            {data: "", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return meta.row+1;
                }
            },
            {data: "billNo", bSortable: false},
            {data: "itemType", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fcBond.itemType",full.itemType);
                }
            },
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: false},
            {
                data: "applyMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    var html = '';
                    html = bt.money.format(full.applyMoney);
                    return html;
                }
            }
        ]
    });

    //转出账号
    var outBankAccInit = bt.selectTable.init({
        elem: "#outBankAccTable",
        url: basePath + "/bond/fcbondtype/getOutBankAcc",
        urldata: {corpId: $("#corpId").val(),curId: $("#curId").val()},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#outBankAcc",
        paging: true,
        columns: [
            {data: "bankAcc",},
            {data: "accName",},
            {data: "bankName",},
            {data:"curName",},
            {data:"isOnline",
                render: function ( data, type, full, meta ) {
                    return bt.dict("sf_yn",full.isOnline);
                }
            }
        ],
        clickCallback: function (data) {
            $("#outBankAcc").val(data.bankAcc);
        }
    });

    var purposeDef=$("#purposeDef").val();
    if(purposeDef == undefined){
        purposeDef = "";
    }
    //获取单位用途
    $.ajax({
        url: basePath+"/basic/business/btpurpose?getPurpose",
        method: 'POST',
        data: {corpId: $("#corpId").val(),sysBusType:4},
        dataType: 'json',
        success: function(data){
            $("#purpose").empty();
            $("#purpose").append("<option value=' '></option><option value='inputText'></option>");
            if(data && data.obj &&  data.obj.length>0){
                $.each(data.obj,function(index,item){
                    if(item.purpose == purposeDef){
                        $("#purpose").append("<option value="+purposeDef+" selected='selected'>"+purposeDef+"</option>");
                    }else {
                        $("#purpose").append("<option value=" + item.purpose + ">" + item.purpose + "</option>");
                    }
                });
            }else{
                $("#purpose").append("<option value=''></option>");
            }
            form.render();
        }
    });


    //监听提交
    form.on('submit(configSubmit)', function(data) {
		var availMoney = moneyType.get("availMoney").val();//可用金额
		var backInterest = moneyType.get("backInterest").val();//转回利息
		var backMoney = moneyType.get("backMoney").val();//实际转回
       // var sumMoney = bt.money.add(availMoney, backInterest);//可用+利息=可转回
		var dif=bt.money.sub(availMoney,backMoney)
		// 判断转回的金额是否大于可用金额加上利息收入
        if(dif<0){
            layer.alert(bt.lang("fcbond.check.BackMoney"),{icon:2});
            return false;
        }
        var action = "/bond/fcbondback/backmoney";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            bt.loading.hide(); //关闭加载中
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                    bt.navtab.refreshParentTable();
                }
            }
        },'JSON');
		return false;
	});

    toRegular();
    getInterest();
});

/**是否存入定期*/
function toRegular() {
    var isRegular = $("#isRegular").val();
    if(isRegular == "1"){
        $(".toRegular").show();
    }else {
        $(".toRegular").hide();
    }
}

function getInterest() {
    var isRegular = $("#isRegular").val();
    var dayLimi = $("#dayLimi").val();
    var rate = $("#rate").val();
    var bondMoney = $("#bondMoney").val();
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    var interest = 0;
    if("1" == isRegular){//定期
        // 如果存入的期限是N年：存入的金额*年利率*期限。
        var limi = moneyFun.div(dayLimi, 365, 2);
        interest = bondMoney*rate*limi-0;
        moneyType.get("backInterest").val(interest);
    }
}

function DownLoadFile(options) {
    var config = $.extend(true, { method: 'post' }, options);
    var $iframe = $('<iframe id="down-file-iframe" style="display: none"/>');
    var $form = $('<form target="_self" method="post" />');
    $form.attr('action', config.url);
    for (var key in config.data) {
        $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    $iframe.append($form);
    $(document.body).append($iframe);
    $form[0].submit();
    $iframe.remove();
    return false;
}