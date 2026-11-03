var dateTypes,moneyTypes,selectTypes,dicts,form;
bt.use(['form','jquery','corp','dateType','moneyType','selectType','dicts','btUpload', 'btImport'], function(){
    $ = bt.jquery;
    form = bt.form(),fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
    moneyTypes = bt.moneyType.init();//金额
    moneyFun = bt.moneyType.getFun();
    corp = bt.corp.init();//单位
    selectTypes = bt.selectType.init();//下拉框
    dicts = bt.dicts.init();//字典

    // moneyFun=bt.moneyType.getFun();

    var gid = $("#attachmentId").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    // 导入
    $("#importLevelMessage").on('click',function () {
        if(!$(this).hasClass('layui-btn-disabled')){
            bt.btImport.init({
                code: "70000040",
                width: "100%",
                height: "100%",
                titleRows: 0,
                headRows: 1,
                success: function (head, data, select) {
                    for (let i = 0; i < select.length; i++) {
                        // 只保留页面选择的发行编号的数据
                        var importData = select[i]
                        addImport(importData.levelId,importData.productName,importData.expectIssSize,importData.timeLimit);
                    }
                    bt.selectType.init();
                    bt.moneyType.init();
                }
            });
        }
    })

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == "null") {
            return true;
        }
        return false;
    }

    function changeLevel(obj) {
        // 只有当旧键存在且新旧键名不相同时才进行操作
        var levelVOList = []
        for (var key in obj) {
            if(key.indexOf('levelVOList') > -1) {
                for(var i = 0; i <200; i++) {
                    if (key.indexOf('levelVOList[' + i + ']') > -1) {
                        if (!levelVOList[i]) {
                            levelVOList[i] = {}
                        }
                        var parts = key.split(/\./)[1]
                        levelVOList[i][parts] = obj[key]
                        delete obj[key]
                    }
                }

            }
        }
        obj.levelVOList = levelVOList.filter((item) => item)
        return obj;
    }
    //监听提交
    form.on('submit', function(data){
        var action = "/cms/security/info/modify"
        data.field.createDate = data.field.createDate.substring(0,19);
        data.field = changeLevel(data.field)
        if (data.field.updateDate != "undefined" && data.field.updateDate != null) {
            data.field.updateDate = data.field.updateDate.substring(0,19);
        }
        data.field.attachmentId = up.getGroupId();
        data.field.status = $(this).val();
        bt.loading.show();
        $.ajax({
            url: basePath + action,
            method: 'POST',
            cache: false,
            data: JSON.stringify(data.field),//将对象序列化成JSON字符串
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                parent.layer.msg(result.msg, {icon: result.success?1:2});
                if(result.success){
                    if(!bt.navtab){
                        location.reload();
                        parent.layer?parent.layer.closeAll():layer.closeAll();
                    } else {
                        if(data.field.id != ""){
                            bt.navtab.closeCurrent();
                        }
                        bt.navtab.refreshCurrent();
                    }

                }
            },
            complete:function () {
                bt.loading.hide();
            },
        });
        // $.post(basePath + action, {body: JSON.stringify(data.field)},function(result){
        //     parent.layer.msg(result.msg, {icon: result.success?1:2});
        //     if(result.success){
        //         if(!bt.navtab){
        //             location.reload();
        //             parent.layer?parent.layer.closeAll():layer.closeAll();
        //         } else {
        //             if(data.field.id == ""){
        //                 bt.navtab.refreshCurrent();
        //             }else{
        //                 bt.navtab.closeCurrent();
        //                 bt.navtab.refreshCurrent();
        //             }
        //
        //         }
        //     }else{
        //         bt.loading.hide();
        //     }
        // },'JSON');
        return false;
    });

});



/**
 * 折合人民币
 */
function conver() {
    var curId = selectTypes.get("curId").val();
    var money = $("#money").val();
    var curPreTime = getTime(new Date());
    if (!bt.empty(curId) && !bt.empty(money) && !bt.empty(curPreTime)) {
        $("#importLevelMessage").removeClass('layui-btn-disabled');
        bt.jquery.ajax({
            url: basePath + "/cms/lease/financing/info/conver/" + curId + "/" + curPreTime,
            method: 'POST',
            dataType: 'json',
            success: function (t) {
                bt.moneyType.init().get("rmbMoney").val(moneyFun.mul(t, money, 2));
            },
        });
    }else{
        $("#importLevelMessage").addClass('layui-btn-disabled')
        // 清空
        $("#rmbMoney").val('').siblings('input').val('');
    }
}

function getTime(date) {
    let y = date.getFullYear() //年
    let m = date.getMonth() + 1  //月，月是从0开始的所以+1
    let d = date.getDate() //日
    m = m < 10 ? "0" + m : m //小于10补0
    d = d < 10 ? "0" + d : d //小于10补0
    return y + "-" + m + "-" + d; //返回时间形式yyyy-mm-dd
}

function addTr() {
    var tab = document.getElementById("dataTable");
    trHtml(tab);
    // var lastRow = $("#dataTable tbody").find("tr:last").data('row');
    bt.moneyType.init();
    bt.selectType.init();
}

trHtml = function (tab) {
    var rowsCount = tab.rows.length;
    var row = rowsCount - 1;
    var trHtml = '<tr data-row="' + row + '">';
    trHtml +=' <td align="center"> <div class="layui-inline"> <div class="bt-biz-selectType ignore-required-icon" data-biz-name="levelVOList['+ row +']levelId"  data-url="'+basePath+'/cms/security/level/listCmsSecurityLevel" data-valuefield="id" data-required="true" data-textfield="levelName" id="levelId'+row+'" data-name="levelVOList['+ row +'].levelId" data-search="true" data-link="true"> </div> </div> </td>';
    trHtml +=' <td align="center"> <input type="text" id="productName'+ row +'" name="levelVOList['+ row +'].productName" autocomplete="off" class="layui-input ignore-required-icon" lay-verify="required|strlength" maxlength="100" data-maxlength="100"></td>';
    trHtml +=' <td align="center"> <input type="text" id="expectIssSize'+ row +'" data-name="levelVOList['+ row +'].expectIssSize" lay-verify="required" data-biz-name="levelVOList['+ row +']expectIssSize" data-callback="sizeof" autocomplete="off" class="layui-input bt-biz-moneyType ignore-required-icon"> </td>';
    trHtml +=' <td align="center" id="total'+ row +'"> </td> <input type="hidden" id="sizeOf'+ row +'" name="levelVOList['+ row +'].sizeOf" >';
    trHtml +=' <td align="center"> <input type="text" id="timeLimit'+ row +'" data-name="levelVOList['+ row +'].timeLimit" data-decimal="2" data-biz-name="levelVOList['+ row +']timeLimit" autocomplete="off" class="layui-input bt-biz-moneyType"> </td>';
    trHtml += '<td align="center"><a href="javascript:void(0);" title="#springMessage("common.delTr")" onClick="remoTr(this.parentElement.parentElement.rowIndex)"><i class="layui-icon">&#xe640;</i></a></td>';
    trHtml += '</tr>';
    $("#dataTable tbody").append(trHtml);

}

function remoTr(rowIndex) {
    var tab = document.getElementById("dataTable");
    tab.deleteRow(rowIndex);
    // console.log(rowIndex);
    //循环修改元素的名字
    // for (var i = rowIndex; i < (tab.rows.length - 1); i++) {
    //     var tempTr = tab.rows.item(i);
        // for (var x = 0; x < tempTr.cells.length; x++) {
        //     var tempTd = tempTr.cells.item(x);
        //     var strHTML = tempTd.innerHTML;
        //     while (strHTML.indexOf("[" + (i - 1) + "]") > 0) {
        //         strHTML = strHTML.replace("[" + (i - 1) + "]", "["
        //             + (i - 2) + "]");
        //     }
        //     while (strHTML.indexOf("[" + (i) + "]") > 0) {
        //         strHTML = strHTML.replace("[" + (i) + "]", "[" + (i - 1)
        //             + "]");
        //     }
        //     console.log(strHTML, tempTr.cells);
        //     tempTd.innerHTML = strHTML;
        // }
    // }
    return false;
}

function sizeof(data, elem) {
    var rowIndex = elem.parents('tr').data('row');
    var sumMoney= moneyTypes.get("money").val();
    var result = 0
    if( bt.empty(sumMoney)){
        layer.alert(bt.lang("请输入计划发行总金额"), {icon: 2});
        moneyTypes.get("levelVOList["+rowIndex+"]expectIssSize").val("");
    }
    else {
        result = moneyFun.div(data,sumMoney)*100
    }
    // var expectIssSize=moneyFun.format(data/sumMoney*100, {decimal: 2})
    var expectIssSize=moneyFun.format(result, {decimal: 2,separator:''})
    $("#total"+rowIndex).html(expectIssSize);
    $("#sizeOf"+rowIndex).val(expectIssSize);
}

function addImport(levelId,productName,expectIssSize,timeLimit) {
    var onemoney=moneyTypes.get("levelVOList[0]expectIssSize").val();
    if(bt.empty(onemoney)){
        remoTr(1);
    }
    var tab = document.getElementById("dataTable");
    var rowsCount = tab.rows.length;
    var sumMoney= moneyTypes.get("money").val();
    if( bt.empty(sumMoney)){
        layer.alert(bt.lang("请输入计划发行总金额"), {icon: 2});
        return;
    }
    var result = moneyFun.div(expectIssSize,sumMoney)*100

    // var sizeOf=moneyFun.format(expectIssSize/sumMoney*100, {decimal: 2});
    var sizeOf=moneyFun.format(result, {decimal: 2,separator:''});
    var row = rowsCount - 1;
    var trHtml = '<tr data-row="' + row + '">';
    trHtml +=' <td align="center"> <div class="layui-inline"> <div class="bt-biz-selectType ignore-required-icon" data-biz-name="levelVOList['+ row +']levelId"  data-url="'+basePath+'/cms/security/level/listCmsSecurityLevel" data-valuefield="id" data-required="true" data-textfield="levelName" id="levelId'+row+'" data-name="levelVOList['+ row +'].levelId" data-search="true" data-link="true" data-defaultvalue="'+levelId+'"> </div> </div> </td>';
    trHtml +=' <td align="center"> <input type="text" id="productName'+ row +'" name="levelVOList['+ row +'].productName" autocomplete="off" class="layui-input ignore-required-icon" lay-verify="required|strlength" maxlength="100" data-maxlength="100" value="'+productName+'"></td>';
    trHtml +=' <td align="center"> <input type="text" id="expectIssSize'+ row +'" data-name="levelVOList['+ row +'].expectIssSize" lay-verify="required" data-biz-name="levelVOList['+ row +']expectIssSize" data-callback="sizeof" autocomplete="off" class="layui-input bt-biz-moneyType ignore-required-icon" value="'+expectIssSize+'"> </td>';
    trHtml +=' <td align="center" id="total'+ row +'">'+sizeOf+' </td> <input type="hidden" id="sizeOf'+ row +'" name="levelVOList['+ row +'].sizeOf" value="'+sizeOf+'">';
    trHtml +=' <td align="center"> <input type="text" id="timeLimit'+ row +'" data-name="levelVOList['+ row +'].timeLimit" data-decimal="2" data-biz-name="levelVOList['+ row +']timeLimit" autocomplete="off" class="layui-input bt-biz-moneyType" value="'+timeLimit+'"> </td>';
    if(row ==0){
        trHtml += '<td align="center"> <a href="javascript:void(0);" title="#springMessage("common.addTr")" onClick="addTr()"><i class="layui-icon">&#xe654;</i></a></td>';
    }else {
        trHtml += '<td align="center"><a href="javascript:void(0);" title="#springMessage("common.delTr")" onClick="remoTr(this.parentElement.parentElement.rowIndex)"><i class="layui-icon">&#xe640;</i></a></td>';
    }
    trHtml += '</tr>';
    $("#dataTable tbody").append(trHtml);
}
