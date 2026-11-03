bt.use(['form', 'dicts','treeGrid','element'], function () {
    $ = bt.jquery, form = bt.form();
    element = parent.bt.element();
    var dicts = bt.dicts.init();
    var addFunc = function (rowData) {
        parent.layer.open({
            type: 2,
            area: ['850px', '550px'],
            maxmin: true,
            content: basePath + "/pool/fcCorpPool/add",
            end: function () {
                treeGrid.trigger("reloadGrid");
            }
        });
    }
    //新增子单位
    var addSub = function (rowData) {
        var id = (rowData && rowData.id) ? rowData.id : '';
        parent.layer.open({
            type: 2,
            area: ['850px', '550px'],
            maxmin: true,
            content: basePath + "/pool/fcCorpPool/add?poolId=" + rowData.id +"&poolSign=" + rowData.poolSign,
            end: function () {
                treeGrid.trigger("reloadGrid");
            }
        });
    }
    //编辑
    var editFunc = function (rowData) {
        var id = rowData.id ? rowData.id : '';
        parent.layer.open({
            type: 2,
            area: ['850px', '550px'],
            maxmin: true,
            content: basePath + "/pool/fcCorpPool/update?id=" + id,
            end: function () {
                treeGrid.trigger("reloadGrid");
            }
        });
    }
    //查看
    var viewFunc = function (rowData) {
        var id = rowData.id ? rowData.id : '';
        parent.layer.open({
            type: 2,
            area: ['850px', '550px'],
            maxmin: true,
            content: basePath + "/pool/fcCorpPool/view/" + id,
            end: function () {
                treeGrid.trigger("reloadGrid");
            }
        });
    }
    //删除
    var delFunc = function (rowData) {
        var id = rowData.id ? rowData.id : '';
        var poolId = rowData.poolId ? rowData.poolId : '';
        var poolSign = rowData.poolSign ? rowData.poolSign : '';
        var corpId = rowData.corpId ? rowData.corpId : '';
        //子单位
        if(poolSign == "0"){
            var childAccFlag;
            $.ajaxSettings.async = false;
            $.get(basePath + "/pool/fcCorpPool/childAcc/" + poolId +"/" + corpId, function (resultMap) {
                childAccFlag = resultMap.childAccFlag;
            }, 'JSON');
            $.ajaxSettings.async = true;
            if(childAccFlag){
                parent.layer.alert(bt.lang('nis.pool.fcCorpPool.has.innerAcc'), {icon: 2});
                return false;
            }else {
                parent.layer.confirm(bt.lang('common.message.confirm.delete'), {icon: 3}, function () {
                    $.get(basePath + "/pool/fcCorpPool/delete/" + id, function (result) {
                        treeGrid.trigger("reloadGrid");
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }, 'JSON');
                });
            }
        }else{
            var poolChildFlag;
            var isUsePool;
            $.ajaxSettings.async = false;
            $.get(basePath + "/pool/fcCorpPool/judgePool/" + id +"/" + poolSign, function (resultMap) {
                poolChildFlag = resultMap.hasChildPool;
                isUsePool = resultMap.isUsePool;
            }, 'JSON');
            $.ajaxSettings.async = true;
            if (poolChildFlag) {
                parent.layer.alert(bt.lang('nis.pool.fcCorpPool.has.child'), {icon: 2});
                return false;
            }else if(isUsePool){
                parent.layer.alert(bt.lang('pool.poolcode.message.has.used'), {icon: 2});
                return false;
            }else{
                parent.layer.confirm(bt.lang('common.message.confirm.delete'), {icon: 3}, function () {
                    $.get(basePath + "/pool/fcCorpPool/delete/" + id, function (result) {
                        treeGrid.trigger("reloadGrid");
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }, 'JSON');
                });
            }
        }
    }

    var treeGrid = bt.treeGrid.init({
        elem: "#corpTable",
        url: basePath + "/pool/fcCorpPool/tree.json",
        ExpandColumn: "corpName",
        colModel: [
            {
                name: "corpName",
                label: bt.lang("nis.pool.fcCorpPool.poolChildCorp"),
                formatter: function (cellvalue, options, rowObject) {
                    return "(" + rowObject.corpCode + ")" + rowObject.corpName;
                }
            },
            {
                name: "poolSign",
                label: bt.lang("table.fcCorpPool.poolSign"),
                width: 30,
                align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    return rowObject.poolSign == '1' ? '<span style="color:red;">' + bt.lang("nis.pool.fcCorpPool.poolCorp") + '</span>' : bt.lang("nis.pool.fcCorpPool.poolsubCorp");
                }
            },
            {name: "poolCode", label: bt.lang("table.fcCorpPool.poolCode"), width: 30, align: "center"},
            {name: "poolName", label: bt.lang("table.fcCorpPool.poolName"), width: 80, align: "center"},
            {
                name: "status",
                label: bt.lang("table.fcCorpPool.status"),
                width: 30,
                align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    return cellvalue == 1 ? bt.lang("common.button.enable") : '<span style="color:#cacaca;">' + bt.lang("common.button.taboo") + '</span>';
                }
            },
            {
                label: bt.lang("table.createRepayPlan.operation"),
                width: 50,
                formatter: function (cellvalue, options, rowObject) {
                    var html = '';
                    if (rowObject.poolSign === "1" && rowObject.status === "1") {
                        html += '<a href="javascript:void(0)" row="' + rowObject.id + '" class="add-sub">' + bt.lang("nis.pool.fcCorpPool.addChildCorp") +'</a>';
                    }
                    html += '&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" row="' + rowObject.id + '" class="view">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            },
        ],
        navPosition: "top",
        addFunc: addFunc,
        editFunc: editFunc,
        delFunc: delFunc,
        viewFunc: viewFunc,
        afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
            treeGrid.trigger("reloadGrid");
        }
    });
    treeGrid.on("click", ".add-sub", function () {
        var data = treeGrid.getOriginalData($(this).attr("row"));
        addSub(data);
    });

    treeGrid.on("click", ".view", function () {
        var data = treeGrid.getOriginalData($(this).attr("row"));
        viewFunc(data);
    });

    $("#search_btn").click(function(){
        var poolCode = $("#poolCode").val();
        var poolName = $("#poolName").val();
        var status = dicts.get('status').val();
        $("#corpTable").jqGrid('setGridParam',{
            url:basePath + "/pool/fcCorpPool/tree.json",
            datatype:'json',
            postData:{'poolCode':poolCode,'poolName':poolName,'status':status}, //发送数据
            page:1
        }).trigger("reloadGrid"); //重新载入
    });
});


