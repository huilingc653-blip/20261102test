var dicts,moneyType
bt.use(['form', 'datatable', 'dicts', 'dateType', 'accountTable', 'selectType','moneyType','corp'], function () {
    $ = bt.jquery, form = bt.form();
    dateType = bt.dateType.init();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    bt.corp.init();

    // 页面加载完成后禁用自动勾对规则第四个复选框
    setTimeout(function() {
        var checkbox = $('input[value="4"][name="eqs_matchingRule"]');
        if (checkbox.length > 0) {
            checkbox.prop('disabled', true);
            checkbox.closest('.layui-unselect').addClass('layui-form-checkbox-disabled');
            form.render('checkbox'); // 重新渲染表单
        }
    }, 300);

    bifCodeCallback = function(data){
        bt.accountTable.get("likes_bankAcc").reload({
            urldata: {
                bifCode: data.id?data.id:""
            }
        });
        $("#likes_bankAcc").val("");
    }

    accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);
    }

    clearAll = function () {
        bt.accountTable.get("likes_bankAcc").reload({
            urldata: {
                bifCode: ""
            }
        });
        $("#likes_bankAcc").val("");
    }

    bt.selectType.init();

    bt.accountTable.init();

    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        ignoreButtons: ['colvis', 'output', 'print'],
        colReorder: true,
        checkbox: true,
        select: false,
        selectCss: false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/receipt/bisbankreceiptimport/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:receipt:bisbankreceiptImport:apply'),
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    add();
                }
            },{
                hide: bt.isnotResource('netbank:receipt:bisbankreceiptImport:apply'),
                text: bt.lang('table.fcCompare.autoCheck'),
                action: function (e, dt, node, config) {
                    runBlending();
                }
            },{
                hide: bt.isnotResource('netbank:receipt:bisbankreceiptImport:apply'),
                text: bt.lang('common.button.total.paymentblending'),
                action: function (e, dt, node, config) {
                    paymentBlending(e, dt, node, config);
                }
            },{
                hide: bt.isnotResource('netbank:receipt:bisbankreceiptImport:apply'),
                text: bt.lang('common.button.cancel.batchblending'),
                className: 'cancelButton',
                action: function (e, dt, node, config) {
                    if (e.currentTarget.className.indexOf('disabled') === -1) {
                        batchCancel(e, dt, node, config);
                    }
                }
            }, {
                hide: bt.isnotResource('netbank:receipt:bisbankreceiptImport:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    batchDelete(e, dt, node, config);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data:null,bSortable:false,className:'center',//生成序号
                render: function (data, type, full, meta) {
                    var html ='';
                    html += '<lable class="" >'+(meta.row+1)+'</lable>'
                    return html;
                }
            },
            {data: "bankTypeName", className: 'center', bSortable: false},
            {data: "corpName", className: 'center', bSortable: false},
            {data: "bankAcc", className: 'center', bSortable: false},
            {
                data: "importDate", className: 'center', bSortable: false
            },
            {data: "fileName", className: 'center', bSortable: false},
            {
                data:"reportText", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return    '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                }},
            // {
            //     data:null, bSortable: false, className: 'center',
            //     render: function (data, type, full, meta) {
            //         var html = '';
            //         if(full.blendSign == '1'){
            //             html += '<a class="" href="#" data-row="' + meta.row + '" onclick="viewDtl(this)">' + bt.lang("common.button.viewFlow") + '</a><span class="text-explode"></span>';
            //         }else{
            //             html += '<a class="" href="#" data-row="' + meta.row + '" onclick="blendDtl(this)">' + bt.lang("common.button.targetFlow") + '</a><span class="text-explode"></span>';
            //         }
            //         return html;
            //     }
            // },
            {data: "amt", bSortable: true,className: 'textRight',
                render: function (data, type, full, meta) {
                    if(full.blendSign == '1'){
                        return bt.money.format(full.amt);
                    } else {
                        return '';
                    }
                }
            },
            {
                data:null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    //流水关联列迁移到操作模块
                    if(full.blendSign == '1'){
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="viewDtl(this)">' + bt.lang("common.button.viewFlow") + '</a><span class="text-explode">|</span>';
                    }else{
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="blendDtl(this)">' + bt.lang("common.button.manual.paymentblending") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="viewFile(this)">' + bt.lang("common.button.excel.previewBill") + '</a><span class="text-explode">|</span>';
                    if(full.blendSign == '1'){
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="cancelColumn(this)">' + bt.lang("common.button.cancel.paymentblending") + '</a><span class="text-explode"></span>';
                    } else {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode"></span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="downloadFile(this)">' + bt.lang("common.button.downloadBill") + '</a><span class="text-explode"></span>';

                    return html;
                }
            }
        ]
    });

    /**
     * 批量删除
     * @param e
     * @param dt
     * @param node
     * @param config
     */
    function batchDelete(e, dt, node, config) {
        var data = dt.rows('.selected').data();
        if (data.length < 1) {
            layer.msg(bt.lang('common.message.confirm.selectData'));
            return;
        }
        var ids = "";
        var rowData = data.splice(0, data.length);
        for (var i = 0; i < rowData.length; i++) {
            if(rowData[i].blendSign == '1'){
                layer.alert(bt.lang('【'+rowData[i].fileName+'】回单已关联流水，请先取消勾兑！'), {icon: 7});
                return false;
            }
            if (ids == "") {
                ids += rowData[i].id;
            } else {
                ids += "," + rowData[i].id;
            }
        }
        parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
            $.post(basePath + "/receipt/bisbankreceiptimport/batchDelete/", {ids: ids}, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
 function paymentBlending(e, dt, node, config) {
        var data = dt.rows('.selected').data();
        if (data.length < 1) {
            layer.msg(bt.lang('common.message.confirm.selectData'));
            return;
        }
        var ids = "";
        var rowData = data.splice(0, data.length);
        for (var i = 0; i < rowData.length; i++) {
            if(rowData[i].blendSign == '1'){
                layer.alert(bt.lang('【'+rowData[i].fileName+'】回单已关联流水，无法二次勾对！'), {icon: 7});
                return false;
            }
            if (ids == "") {
                ids += rowData[i].id;
            } else {
                ids += "," + rowData[i].id;
            }
        }
     layer.open({
         type: 2,
         area: ['1000px','800px'],
         maxmin: true,
         closeBtn: 1,
         content: basePath + "/receipt/bisbankreceiptimport/listAccDtl/"+ids,
         end : function(){
         }
     });
    }

    //取消勾兑
    function batchCancel(e, dt, node, config) {
        var data = dt.rows('.selected').data();
        if (data.length < 1) {
            layer.msg(bt.lang('common.message.confirm.selectData'));
            return;
        }
        var dtlIds = "";
        var rowData = data.splice(0, data.length);
        for (var i = 0; i < rowData.length; i++) {
            if(!rowData[i].blendSign || rowData[i].blendSign != '1'){
                layer.alert(bt.lang('【'+rowData[i].fileName+'】回单未关联流水，无法取消勾兑！'), {icon: 7});
                return false;
            }
            if (dtlIds == "") {
                dtlIds += rowData[i].accDtlId;
            } else {
                dtlIds += "," + rowData[i].accDtlId;
            }
        }
        parent.layer.confirm(bt.lang('common.message.confirm.cancelmanual'), function () {
            $.post(basePath + "/receipt/bisbankreceiptimport/batchCancleBlend/", {dtlIds: dtlIds}, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/receipt/bisbankreceiptimport/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/receipt/bisbankreceiptimport/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}
// 预览页面加载标识
var isViewWebLoading = false;

function viewDtl(el){
    if(isViewWebLoading){
        layer.msg(bt.lang('sys.notification.loading'))
        return
    }
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    isViewWebLoading = true;
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccdtl/view/" + data.accDtlId+",rules",
        end:function () {
            isViewWebLoading = false;
        }
    });

}

function blendDtl(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    layer.open({
        type: 2,
        area: ['1000px','800px'],
        maxmin: true,
        closeBtn: 1,
        content: basePath + "/receipt/bisbankreceiptimport/listAccDtl/"+data.id+"/"+data.bankAcc,
        end : function(){
        }
    });
}

function viewFile(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data && data.attachmentId){
        $.ajax({
            url:basePath+"/attachment/sysattachment/preview_from_path?id="+data.attachmentId,
            type: "GET",
            success: function (res) {
                var url=JSON.parse(res).url;
                var type=JSON.parse(res).type;
                // 'pdf',
                var extArr = [ 'txt','xlsx', 'pptx','pdf','ofd', 'docx','doc','png', 'jpg', 'jpeg','ppt','xls','doc']
                if (extArr.indexOf(type) > -1) {
                    top.layer.open({
                        title: "预览",
                        type: 2,
                        area: ['1000px', '640px'],
                        content: (type == 'txt' || type == 'pdf')? url:basePath+'/sys/resources/lookAnnex?url='+url,
                        cancel:function () {
                            layer.close();
                        }
                    });
                }else {
                    layer.msg('预览支持格式为  .txt,.xlsx, .pptx, .docx, .pdf , .png, .jpg, .jpeg, .ppt, .xls, .doc' )
                    return false
                }
            },
            error:function(){
                console.log(arguments)
            }
        })
    }else{
        layui.layer.msg("请先上传后再预览");
    }

}


function downloadFile(el){
         var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        window.location = basePath+"/attachment/sysattachment/download?id="+data.attachmentId;
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/receipt/bisbankreceiptimport/view/" + data.id
    });
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var msg = bt.lang('common.message.confirm.delete');
    parent.layer.confirm(msg, function () {
        $.post(basePath + "/receipt/bisbankreceiptimport/delete/" + data.id , function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function returnMsg(msg){
    parent.layer.alert(msg, {icon: 1});
}

function cancelColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var msg = bt.lang('common.message.confirm.cancelmanual');
    parent.layer.confirm(msg, function () {
        $.post(basePath + "/receipt/bisbankreceiptimport/cancleBlend/" + data.accDtlId, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}


//根据所选银行流水获取匹配的电子回单信息
function getReceipt() {
    var bankAccTypeStr = '';
    var flag = false;
    $('input[name="eqs_matchingRule"]:checked').each(function () {
        bankAccTypeStr += $(this).val() + ",";
        if ($(this).val() === '4') {
            flag = true;
        }
    });
    if(flag){
        $("#transTimeRuleTr").show();
    }else{
        $("#transTimeRuleTr").hide();
    }
}
function transTimeRuleCallback(){
    var array = dicts.get('transTimeRule').val()
    var flag = false;
    if (array) {
        for (var i = 0; i < array.length; i++) {
            if (array[i] === '1') {
                flag = true;
            }
        }
    }
    moneyType.get("transDays").val("0");
    if(flag){
        $("#tranDaysDiv").show();
    }else{
        $("#tranDaysDiv").hide();
    }
}

function runBlending() {
    //字段选择
    var array = dicts.get('matchingRule').val()

    var matchingRule ='';
    if (array) {
        for (var i = 0; i < array.length; i++) {
            matchingRule+=array[i] + ",";
        }
    }
    matchingRule = matchingRule.substring(0, matchingRule.length - 1)

    //是否交易日期相等
    var transTimeRule = dicts.get('transTimeRule').val()
    //交易日期不相等偏差天数
    var transDays=$('input[name="transDays"]').val()

    //交易开始日期和结束日期
    var ged_tradeTime=$('input[name="ged_tradeTime"]').val()
    var led_tradeTime=$('input[name="led_tradeTime"]').val()

    if (ged_tradeTime==null||ged_tradeTime==''){
        parent.layer.alert(bt.lang("billPool.transDate.isNotNull"), {icon: 2});
        return;
    }
    if (led_tradeTime==null||led_tradeTime==''){
        parent.layer.alert(bt.lang("billPool.transDate.isNotNull"), {icon: 2});
        return;
    }
    console.log(matchingRule);
    console.log(transTimeRule);
    console.log(transDays);
    console.log(ged_tradeTime);
    console.log(led_tradeTime);

    var data = {};
    data.matchingRule=matchingRule;
    data.transTimeRule=transTimeRule;
    data.transDays=transDays;
    data.ged_tradeTime=ged_tradeTime;
    data.led_tradeTime=led_tradeTime;

    bt.jquery.ajax({
        url: basePath + "/receipt/bisbankreceiptimport/runBlending",
        method: 'POST',
        data: JSON.stringify(data),//将对象序列化成JSON字符串
        contentType: 'application/json',
        success: function (res) {
            var result = JSON.parse(res);
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        },
    })
   //  $.post(basePath + "/receipt/bisbankreceiptimport/runBlending", JSON.stringify(data), function (result) {
   //    bt.datatable.refreshCurrentPage();
   //    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
   // }, 'JSON');
}
