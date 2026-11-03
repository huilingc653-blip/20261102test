bt.use(['form', 'ligerTreeTable', 'dicts', 'mulselect'], function () {
    var $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();
    var idIndex = 1;
    bt.mulselect.init();
    var currentTableId = "appTable"; // 保存当前表格ID


    queryData();
    $("#search").click(function () {
        idIndex++
        currentTableId = "appTable_" + idIndex; // 更新当前表格ID
        $("#treeTableBox").html('<div id="appTable_' + idIndex + '"></div><div class="layui-hide" id="treeEmpty"> <img src="'+basePath+'/static/v3.2/images/pic_blank.png" class="pic-bank"><div class="empty-text">'+bt.lang("common.msg.temporarily_no_data")+'</div></div>')
        queryData(true)
    });
    function queryData(reRender) {
        $('#treeEmpty').addClass('layui-hide');
        var templateNo = $("#templateNo").val();
        var templateName = $("#templateName").val();
        var validSign =  $("select[name='validSign']").val();
        var bankTypeId=   bt.mulselect.get("bankTypeId").val()
        var  bifId =bt.mulselect.get("bifId").val()
        var  countryId=   bt.mulselect.get("countryId").val()
        var payTypeName = $("#payTypeName").val();
        var data = {}
        templateNo?data.templateNo = templateNo:   data.templateNo=null;
        templateName?  data.templateName = templateName:  data.templateName ==null;
        validSign && validSign.trim() ? data.validSign = validSign:   data.validSign = null;
        payTypeName?   data.payTypeName = payTypeName:data.payTypeName =null;
        data.bankTypeIds = getIdList(bankTypeId);
        data.bifIds = getIdList(bifId);
        data.countryIds = getIdList(countryId);
        $.ajax({
            url: basePath + "/foreign/fexTranConfigTemplate/datatable",
            type: "POST",
            contentType: "application/json",  // 设置请求头为 JSON
            data: JSON.stringify(data),       // 将数据转为 JSON 字符串
            dataType: "json",                 // 期望返回的数据类型
            success: function(result) {
                if (result) {
                    initTreeData(result.rows);
                    initTreeTable(result, reRender);
                    if(result.rows && result.rows.length === 0){
                        $('#treeEmpty').removeClass('layui-hide');
                    }
                }
            },
            error: function(xhr, status, error) {
                console.error("请求失败:", error);
            }
        });
    }

    function initTreeData(data) {
        if (data) {
            bt.each(data, function (i, list) {
                if (list.children && list.children.length === 0) {
                    delete list.children
                } else {
                    if (list.children && list.children.length > 0) {
                        initTreeData(list.children)
                    }
                }
            })
        }

    }


    function initTreeTable(result,reRender) {
        var tableId = reRender?'#appTable_'+idIndex:'#appTable';
        if (!reRender) {
            currentTableId = "appTable";
        }
        bt.ligerTreeTable.initMulTreeTable({
            elem:tableId,
            rownumbers: false,
            isDoubleHeader: false,//是否有双表头
            allowHideColumn: false,
            resize:true,
            columns:[
                {
                    "display": bt.lang("table.configTemplate.templateCode"),
                    "name": "templateNo",
                    "width": 100,
                    "align": "left",
                    // "frozen": true
                },
                {
                    "display": bt.lang("table.fexExposureTemplate.templateName"),
                    "name": "templateName",
                    "width": 143,
                    "align": "left",
                   // "frozen": true
                },
                {
                    "display": bt.lang("table.btswiftinfo.bankType"),
                    "name": "bankTypeName",
                    "align": "center",
                    render: function (record, rowindex, value, column) {
                         var content="";
                        if(value){
                            content=value;
                        }
                        return '<div title="' + content + '">' + content + '</div>'
                    }
                },
                {
                    "display": bt.lang("table.btBankAcc.bifCode"),
                    "name": "bifName",
                    "align": "center",
                    render : function (record, rowindex, value, column) {
                        var content="";
                        if(value){
                            content=value;
                        }
                        return '<div title="' + content + '">' + content + '</div>'
                    }
                },
                {
                    "display": bt.lang("table.btswiftinfo.countryCode"),
                    "name": "countryZhName",
                    "align": "center",
                    render : function (record, rowindex, value, column) {
                        var content="";
                        if(value){
                            content=value;
                        }
                        return '<div title="' + content + '">' + content + '</div>'
                    }
                },
                {
                    "display": bt.lang("table.fexForeign.payType"),
                    "width": 200,
                    "name": "payTypeName",
                    "align": "center",
                    render : function (record, rowindex, value, column) {
                        var content="";
                        if(value){
                            content=value;
                        }
                        return '<div title="' + content + '">' + content + '</div>'
                    }
                },
                {
                    "display": bt.lang("table.cff.validSign"),
                    "width": 60,
                    "name": "validSignName",
                    "align": "center"
                },
                {
                    "display": bt.lang("table.common.operation"),
                    "name": "operation",
                    "align": "center",
                    "width": 250,
                    // frozen: true,
                    render : function (record, rowindex, value, column) {
                        //this     这里指向grid
                        //record   行数据
                        //rowindex 行索引
                        //value    当前的值，对应record[column.name]
                        //column   列信息
                        var buttonHtml = "";
                        if (record.parentId == null ) {
                            buttonHtml += '<a class="" href="#" onclick="configPage(\'' + record.id + '\')">' + bt.lang("fexTranConfig.page") + '</a>' ;
                            if (record.validSign === '1') {
                                // 生效
                                buttonHtml += '<span class="text-explode">|</span>' +
                                    '<a class="" href="#" onclick="disable(\'' + record.id + '\')">' + bt.lang("common.button.disable") + '</a>';
                            } else {
                                //失效
                                buttonHtml += '<span class="text-explode">|</span>' +
                                    '<a class="" href="#" onclick="enable(\'' + record.id + '\')">' + bt.lang("common.button.enable") + '</a>';
                            }
                            buttonHtml += '<span class="text-explode">|</span>' +
                                '<a class="" href="#" onclick="update(\'' + record.id + '\')">' + bt.lang("common.button.edit") + '</a>' +
                                '<span class="text-explode">|</span>' +
                                '<a class="" href="#" onclick="view(\'' + record.id + '\')">' + bt.lang("common.button.view") + '</a>' ;
                            if (!record.children || record.children.length === 0) {
                                buttonHtml +=
                                    '<span class="text-explode">|</span>'+
                                    '<a class="" href="#" onclick="del(\'' + record.id + '\')">' + bt.lang("common.button.delete") + '</a>'
                               ;
                            }
                        } else {
                            if (record.validSign === '1') {
                                //生效
                                if(record.isSplit){
                                    buttonHtml += '<span class="text-explode">|</span>' +
                                        '<a class="" href="#" onclick="split(\'' + record.id + '\')">' + bt.lang("common.button.split") + '</a>' ;
                                }
                                buttonHtml +=  '<span class="text-explode">|</span>' +
                                    '<a class="" href="#" onclick="disable(\'' + record.id + '\')">' + bt.lang("common.button.disable") + '</a>'
                                ;
                            } else {
                                //失效
                                if (record.validSignParent === '1') {
                                    buttonHtml += '<a class="" href="#" onclick="enable(\'' + record.id + '\')">' + bt.lang("common.button.enable") ;

                                }
                             }
                            buttonHtml += '</a>'+'<span class="text-explode">|</span>'+
                                '<a class="" href="#" onclick="del(\'' + record.id + '\')">' + bt.lang("common.button.delete") + '</a>';
                        }
                        return buttonHtml

                    }
                }
            ],
            data:{
                Rows:result.rows
            },
            tree: { columnName: 'templateNo' },
            onRendered: function() {
                var grid = $(tableId).ligerGetGridManager();
                if (grid) {
                    window.currentGrid = grid;
                    window.currentTableId = tableId.substring(1); // 去掉#号
                    console.log("Grid 实例已保存", grid);
                }}
        },true);
    }


    function openLayer(url) {
        parent.layer.open({
            type: 2,
            area: ['900px', '530px'],
            maxmin: true,
            content: basePath + url,
            end: function () {
            }
        });
    }

    /**
     * 添加子科目
     * @param id
     */
    add = function () {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fexTranConfigTemplate/add");
            return;
        }
        openLayer("/foreign/fexTranConfigTemplate/add")
    };




    /**
     * 编辑
     * @param id
     */
    update = function (id) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexTranConfigTemplate/update/" + id);
            return;
        }
        openLayer("/foreign/fexTranConfigTemplate/update/" + id)
    };

    /**
     * 编辑
     * @param id
     */
    view = function (id) {
        openLayer("/foreign/fexTranConfigTemplate/view/" + id)
        return;
    };

    /**
     * 删除
     * @param id
     */
    del = function (id) {
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/foreign/fexTranConfigTemplate/delete/" + id, function (result) {
                if (result.success) {
                    refreshTreeTable();
                }
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    };

    /**
     * 生效
     * @param id
     */
    enable = function (id) {
        parent.layer.confirm(bt.lang('common.message.confirm.batchEnable'), function () {
            $.post(basePath + "/foreign/fexTranConfigTemplate/validTrue/" + id, function (result) {
                if (result.success) {
                    refreshTreeTable();
                }
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    };

    /**
     * 失效
     * @param id
     */
    disable = function (id) {
        parent.layer.confirm(bt.lang('billPool.Pledgee.msg.taboo'), function () {
            $.post(basePath + "/foreign/fexTranConfigTemplate/validFalse/" + id, function (result) {
                if (result.success) {
                    refreshTreeTable();
                }
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    };

    /**
     * 交易页面配置
     * @param id
     */
    configPage = function (id) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexTranReqConfig/addTemplateConfig/" + id);
            return;
        }
        openLayer("/foreign/fexTranReqConfig/addTemplateConfig/" + id)
    }

    /**
     * 拆分
     * @param id
     */
    split = function (id) {
        parent.layer.confirm(bt.lang('common.message.confirm.toSplit'), function () {
            $.post(basePath + "/foreign/fexTranConfigTemplate/split/" + id, function (result) {
                if (result.success) {
                    refreshTreeTable();
                }
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    };

    batchChooseCustFields = function (id) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.fexForeign.addCustFields'), basePath + "/foreign/fexTranReqConfig/batchChooseCustFields/" + id);
            return;
        }
        openLayer("/foreign/fexTranReqConfig/batchChooseCustFields/" + id)
    };


    function getIdList(list) {
        var arr = [];
        for (var i = 0; i < list.length; i++) {
            if(list[i]&&list[i].id){
                arr.push(list[i].id);
            }
        }
        return arr;  // 返回数组，可以被正确序列化
    }



});


function refreshTreeTable() {
    $("#search").trigger('click');
}


//选择银行类型回调
function bankTypeIdCallBack(selectedData) {
    // 获取选中的银行类型ID
    var bankTypeValue = bt.mulselect.get("bankTypeId").val();
    var bankTypeId = bankTypeValue && bankTypeValue.length > 0
        ? bankTypeValue.map(function(item) { return item.id; }).join(',')
        : null;
    var bifMulselect = bt.mulselect.get("bifId");
    if (bankTypeId && bankTypeId !== "0" && bankTypeId !== "") {

        var newUrl = basePath + "/basic/business/bisbifinit/getByBankType/" + bankTypeId;
        // 使用组件的 attr 方法修改 URL
        bifMulselect.attr({
            "data-url": newUrl
        });
    }else {

        var newUrl = basePath + "/basic/business/bisbifinit/getByBankType/" + 0;
        // 使用组件的 attr 方法修改 URL
        bifMulselect.attr({
            "data-url": newUrl
        });
    }
    // ：重置组件
    var $bifId = $("#bifId");
    $bifId.removeClass("init").empty();
    bt.mulselect.render('reset');

}
// 展开所有节点
var flag = 0
function expandAll() {
    // 使用保存的当前表格ID
    var tableId = window.currentTableId || "appTable";
    var grid = $("#" + tableId).ligerGetGridManager();

    if (!grid) {
        // 降级方案：查找所有可能的表格
        var $activeTable = $("[id^='appTable_']:visible, #appTable:visible");
        if ($activeTable.length) {
            grid = $activeTable.ligerGetGridManager();
        }
    }



    if (grid ) {
        if(!flag){
            grid.expandAll();
            flag++
        }else{
            grid.collapseAll();
            flag --
        }
    } else {
        console.warn("无法获取 grid 实例");
    }
}

// 收起所有节点
function collapseAll() {
    var tableId = window.currentTableId || "appTable";
    var grid = $("#" + tableId).ligerGetGridManager();

    if (!grid) {
        var $activeTable = $("[id^='appTable_']:visible, #appTable:visible");
        if ($activeTable.length) {
            grid = $activeTable.ligerGetGridManager();
        }
    }

    if (grid && grid.collapseAll) {
        grid.collapseAll();
    } else {
        console.warn("无法获取 grid 实例");
    }
}