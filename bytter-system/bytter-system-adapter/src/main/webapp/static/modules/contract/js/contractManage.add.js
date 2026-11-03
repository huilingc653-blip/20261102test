bt.biz = function (bizName, pluginName) {
    return window.biz[bizName][pluginName];
}
var sortRowNum = 1;
var form;
var selectTypes;
bt.use(['form', 'jquery', 'corp', 'select', 'selectType', 'bttable', 'bankInfo', 'dicts', 'externalCorpBankAcc', 'selectTableType', 'swiftInfo'], function () {
    form = bt.form(), $ = bt.jquery;
    var layer = parent ? parent.bt.layer : bt.layer;
    //数据来源，用到字典
    bt.dicts.init();
    var corp = bt.corp.init();
    var externalCorpBankAcc = bt.externalCorpBankAcc.init();
    selectTypes = bt.selectType.init();//下拉框
    var bankInfo = bt.bankInfo.init();
    var swiftInfo = bt.swiftInfo.init();
    var bankAccTable;

    form.on('switch(checkCorp)', function (data) {
        if (!this.checked) {
            $("#inputCorp").find('.select-tree-input').attr("lay-verify", "required");
            $("#inputCorp").find('.select-tree-input').removeAttr("disabled");
            $(".celRow").show();
//            $("input[name='shareSign']").removeAttr("lay-verify");
        } else {
            $("#inputCorp").find('.select-tree-input').val('');
            $("#inputCorp").find('.select-tree-input').removeAttr("lay-verify");
            $("#inputCorp").find('.select-tree-input').attr("disabled", "disabled");
            bt.corp.get("corpId").val("");
            $(".celRow").hide();
//            $("input[name='shareSign']").attr("lay-verify","required");
        }
        form.renderIcon();
    });

    if ($("#isCommonValue").val() !== "") {
        initCorpAndisCommon($("#isCommonValue").val());
    }

    form.on('submit(submitEvent)', function (data) {
        submitForm(data);
    });

    //取消按钮
    $("#cancel").click(function () {
        bt.navtab.closeCurrent();
        return false;
    });

    //提交方法
    function submitForm(data) {
        //是否共享
        if (typeof (data.field.shareSign) == "undefined") {
            data.field.shareSign = 0;
        } else if (data.field.shareSign == "on") {
            data.field.shareSign = 1;
        } else {
            data.field.shareSign = 0;
        }

        //是否有效
        if (typeof (data.field.validSign) == "undefined") {
            data.field.validSign = 0;
        } else if (data.field.validSign == "on") {
            data.field.validSign = 1;
        } else {
            data.field.validSign = 0;
        }
        var action = $('input[name="id"]').val() ? "update" : "save";
        if (bt.corp.get("corpId").data() > 0) {
            data.field.corpName = bt.corp.get("corpId").data()[0].corpName
            data.field.corpCode = bt.corp.get("corpId").data()[0].code
        }
        $.post(basePath + '/contract/contractType/' + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                layer.closeAll();
                if (result.success) {
                    if (!bt.navtab) {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    } else {
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshParent();
                    }
                }
            });
        }, 'JSON');
        return false;
    }

    //点击swift后回调
    swiftInfoCallback = function (data, dom) {
        var tr = dom.parents('tr');
        var tds = dom.parents('tr').children('td');

        var index = tr.attr("row");
        tds.eq(1).find('textarea').val(data.bankName);
        tds.eq(2).find('input').val(data.swiftCode);
        tds.eq(4).find('input').val(data.cityName);
        $('#accProvince' + index + '').val(data.countryCode);
        $('#accProvinceFex' + index + '').val(data.nameZh);
    }

    var extenrnalId = $('input[name="id"]').val();
    var rowTr = 0;

    var addRow = function (index) {
        var isCity = addBankAcc(index);
    }

    //国内账号表格新增行
    addBankAcc = function (index) {
        var html = '<tr row="' + index + '">';
        html += '<td style="text-align: center;"><input type="text" lay-verify="required" name="conTypeFieldVO[' + index + '].fieldName" autocomplete="off" class="layui-input externalAcc" /></td>';
        html += '<td style="text-align: center;"><textarea name="conTypeFieldVO[' + index + '].rmk" class="layui-textarea"></textarea></td>';
        html += '<td style="text-align: center;"><input type="checkbox" class="defaultAcc" name="conTypeFieldVO[' + index + '].requiredSign" lay-skin="switch" checked lay-filter="defaultAcc" lay-text="' + bt.lang("common.sc_yn.yes") + '|' + bt.lang("common.sc_yn.no") + '"></td>';
        html += '<td><div class="bt-biz-dict" data-required="true" data-defaultvalue="01" data-type="select" id="fieldType" name="conTypeFieldVO[' + index + '].fieldType" data-name="conTypeFieldVO[' + index + '].fieldType" data-group="CON_TYPE_FIELD_TYPE"></div></td>';
        html += '<td style="text-align: center;"><input type="checkbox" class="defaultAcc" name="conTypeFieldVO[' + index + '].validSign" lay-skin="switch" checked lay-filter="defaultAcc" lay-text="' + bt.lang("common.sc_yn.yes") + '|' + bt.lang("common.sc_yn.no") + '"></td>';
        html += '<td style="text-align: center;"><input type="text" name="conTypeFieldVO[' + index + '].sortNum" class="sortRow" value="' + (sortRowNum++) + '" disabled/></td>';
        html += '<td><a href="javascript:void(0);" class="delete-bankAcc">' + bt.lang("common.button.delete") + '</a></td>';
        html += '</tr>';
        $("#bankAcc").append(html);
        form.render();
        //数据来源，用到字典
        bt.dicts.init();
    }

    var columnsTab = function () {
        var isCity = !$('#bankAccY').hasClass('hide');
        var columns = "";
        if (isCity) {
            columns = [
                {
                    data: "externalAcc",
                    render: function (data, row) {
                        return '<input type="text" lay-verify="required" name="externalAcc[' + row + '].externalAcc"  lay-verify="required" value="' + data.externalAcc + '" autocomplete="off" class="externalAcc layui-input" />';
                    }
                },
                {
                    data: "externalCorpId",
                    render: function (data, row) {
                        return '<input type="hidden" name="externalAcc[' + row + '].externalCorpId" value="' + data.externalCorpId + '" /><textarea name="externalAcc[' + row + '].bank" readonly class="layui-textarea">' + data.bank + '</textarea>';
                    }
                },
                {
                    data: "bankSourceCode",
                    render: function (data, row) {
                        return '<div class="layui-input-inline"><input type="text" name="externalAcc[' + row + '].bankSourceCode" autocomplete="off" lay-verify="required" readonly class="layui-input" value="' + data.bankSourceCode + '"><div class="bt-div-search"><button class="bt-biz-swiftInfo" type="button" data-callback="swiftInfoCallback"><i class="layui-icon">&#xe615;</i></button></div></div>';
                    }
                },
                {
                    data: 'province',
                    render: function (data, row) {
                        return '<div class="layui-input-inline"><input id="accProvince' + row + '" name="externalAcc[' + row + '].accProvince" type="hidden" value="' + data.accProvince + '"></div><div class="layui-input-inline"><input type="text" lay-verify="required" id="accProvinceFex' + row + '" name="externalAcc[' + row + '].accProvinceFex" lay-verify="required" class="layui-input" data-biz-name="accProvinceFex' + row + '" value="' + data.accProvinceFexName + '"></div>';
                    }
                },
                {
                    data: 'city',
                    render: function (data, row) {
                        return '<input type="text" lay-verify="required" name="externalAcc[' + row + '].accCity" autocomplete="off" class="layui-input" value="' + data.accCity + '"/>';
                    }
                },
                {
                    data: 'remark',
                    render: function (data, row) {
                        return '<textarea class="layui-textarea" name="externalAcc[' + row + '].remark">' + (data.remark ? data.remark : "") + '</textarea>';
                    }
                },
                {
                    data: 'isDefaultAcc',
                    render: function (data, row) {
                        return '<input type="checkbox" class="defaultAcc" name="externalAcc[' + row + '].isDefaultAcc" ' + (data.isDefaultAcc == 1 ? 'checked' : '') + ' lay-skin="switch" lay-filter="defaultAcc" lay-text="' + bt.lang("common.sc_yn.yes") + '|' + bt.lang("common.sc_yn.no") + '">';
                    }
                },
                {
                    data: null,
                    render: function (data, row) {
                        return '<a href="javascript:void(0);" class="delete-bankAcc">' + bt.lang("common.button.delete") + '</a>';
                    }
                }
            ];
        }
        return columns;
    }

    //初始化表格
    bankAccTable = bt.bttable.init({
        scrollX: true,
        scrollY: "400px",
        elem: "#bankAcc",
        url: basePath + "/sys/externalCorp/bankAcc.json?id=" + extenrnalId,
        buttons: [
            {
                text: '<i class="fa fa-plus" aria-hidden="true"></i>' + bt.lang('common.addTr'),
                action: function ($button) {
                    var index = rowTr;
                    addRow(index);
                    rowTr++;
                }
            }
        ],
        columns: columnsTab(),
        delRowCallback: function (data, $tr) {
            $tr.remove();
        },
        tableInitCallback: function (data) {
            form.render();
        }
    });

    $(document).on('click', ".delete-bankAcc", function () {
        var tr = $(this).parents('tr');
        var index = tr.attr("row");

        tr.remove();
        sortRowNum = $("input[class='sortRow']").length;
        sortRowNum++;
        $("input[class='sortRow']").each(function (index) {
            $(this).val(index + 1);
        });


    });

    function initCorpAndisCommon(isCommon) {
        if (isCommon == "all") {
            var currentPower = $("#currPower").val()
            if (currentPower == "all" || currentPower == "common") {
                $("#inputCorp").find('.select-tree-input').attr("disabled", "disabled");
                $("input[name='shareSign']").prop("checked", true);
                $(".celRow").hide();
            } else {
                $("input[name='shareSign']").prop("checked", false);
                $("#inputCorp").find('.select-tree-input').attr("lay-verify", "required");
                $(".celRow").show();
            }
        } else if (isCommon == "corp") {
            $("#inputCorp").find('.select-tree-input').attr("lay-verify", "required");
            $("input[name='shareSign']").prop("checked", false);
            $("input[name='shareSign']").attr("disabled", "disabled");
            $(".celRow").show();
        } else if (isCommon == "common") {
            $("#inputCorp").find('.select-tree-input').attr("disabled", "disabled");
            $("input[name='shareSign']").attr("disabled", "disabled");
            $("input[name='shareSign']").prop("checked", true);
            $(".celRow").hide();
        }

        form.renderIcon();
    }


});


