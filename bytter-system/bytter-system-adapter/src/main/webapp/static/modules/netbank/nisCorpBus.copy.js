initCopyedBusType();

function initCopyedBusType() {
    var busType = $('#copyedNisBusTypes').val();
    if (!busType || busType.length == 0) {
        return;
    }
    var busTypes = busType.split(",")

    for (var i = 0; i < busTypes.length; i++) {
        $('#' + busTypes[i]).attr('checked', true);
    }
    $('#copyedNisBusTypes').val('');
}

bt.use(['form', 'layedit', 'selectTree'], function () {
    var $ = bt.jquery, form = bt.form();

    var defaultFormObj = {}
    var hideInputs = $('.hide-input')
    for (var i = 0; i < hideInputs.length; i++) {
        var name = hideInputs[i].name;
        var value = hideInputs[i].value;
        defaultFormObj[name] = value
    }
    hideInputs.remove()
    // 解决页面宽度变化时，造成页面卡顿
    $('#busTypeTable').width($('#busTypeTable').width())
    //下拉树
    $.each($("div[type='selecttree']"), function (i, item) {
        var filter = {
            "enable": true
        };
        if ($(item).attr("dictField")) {
            filter.idKey = $(item).attr("dictField");
        }
        if ($(item).attr("dictText")) {
            filter.pIdKey = $(item).attr("dictText");
        }
        bt.selectTree.init({
            url: $(item).attr("fieldHref"),
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            selected: $(item).attr("defaultVal"),
            setting: {
                data: {
                    simpleData: filter
                }
            }
        });
    });


    //初始化单位下拉树
    bt.selectTree.init({
        url: basePath + "/sys/corp/tree?resource=" + 'netbank:basicdata:niscorpbus:apply',
        elem: ".corpTree",
        name: 'corpId',
        checkType: "check",
        required: true,
        placeholder: bt.lang('common.placeholder.select'),
        textStyle: function (treeId, treeNode) {
            if (defaultFormObj[treeNode.id]) {
                return {color: ""};
            } else {
                return {background: "#81A5ED"};//未分配单位标红
            }
        },
        beforeInit: function (nodes) {
            childrenfor(nodes);
            return nodes;
        },
        setting: {
            check: {
                chkboxType: {"Y": "s", "N": "s"}
            }
        },
        clickCallBack: function (id) {
            var child = $('input[name="busIds"]');

            if (id != "" && id.length <= 32) {
                $.ajax({
                    url: basePath + "/netbank/niscorpbus?getBusIdsByCorpId",
                    method: 'POST',
                    data: {corpId: id},
                    dataType: 'json',
                    success: function (data) {
                        if (data) {
                            var arr = data.obj;
                            child.each(function (index, item) {
                                if (arr && arr.length) {
                                    var isFind = false
                                    for (var i = 0; i < arr.length; i++) {
                                        if (arr[i] == item.value) {
                                            item.checked = true;
                                            isFind = true
                                            break
                                        }
                                    }
                                    if (!isFind) {
                                        item.checked = false;
                                    }
                                } else {
                                    item.checked = false;
                                }
                            });
                        }
                        setCheckAll()
                        // 判断是滞全选
                        // form.render('checkbox');
                    }
                });
            } else {
                child.each(function (index, item) {
                    item.checked = false;
                });
                $("select[name='defVoucherCode']").val("");
                $("#allChoose")[0].checked = false;
                //form.render('checkbox');
            }
        }
    });

    function childrenfor(data) {
        for (k in data) {
            data[k].name = "(" + data[k].code + ")" + data[k].name;
            if (data[k].children != null) {
                childrenfor(data[k].children);
            }

        }
    }

    // 监听提交
    $('#submit').click(function () {
        var busIds = [];
        var field = {
            // 获取下拉树 corpId值
            corpId: $('input[name="corpId"]').val()
        }

        $('form').find('input[name="busIds"]:checked').each(function () {
            busIds.push($(this).val());
        });
        if (busIds) {
            field.busIds = busIds;
        }
        //将隐藏的input值合并到data.field中
        Object.assign(field, defaultFormObj)

        if (field.corpId == "") {
            layer.msg(bt.lang('nis.assign.message.corpid.null'));
            return false;
        }
        if (busIds.length < 1) {
            busIds.push("0");
        }

        var action = "/netbank/niscorpbus/save";
        $.post(basePath + action, field, function (result) {
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.refreshParent();
                    bt.navtab.refreshCurrent()
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
            parent.layer ? parent.layer.alert(result.msg, {
                icon: result.success ? 1 : 2
            }) : layer.alert(result.msg, {
                icon: result.success ? 1 : 2
            });
        }, 'JSON');
        return false;
    })

    //全选
    $('#allChoose').click(function () {
        var that = this;
        var child = $('input[name="busIds"]');
        child.each(function (index, item) {
            item.checked = that.checked;
        });
    })
    // 单选时 对全选进行设置
    $('#busTypeTable').on('click', 'tbody input', function () {
        if (!this.checked) {
            $('#allChoose')[0].checked = this.checked
        } else {
            setCheckAll()
        }
    })

    // 全选联动

    function setCheckAll() {
        try {
            $('#allChoose')[0].checked = $('input[name="busIds"]').length === $('input[name="busIds"]:checked').length
        } catch (e) {

        }
    }
});


