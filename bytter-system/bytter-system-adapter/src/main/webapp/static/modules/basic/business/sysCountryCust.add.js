bt.use(['form','layedit','dicts','selectType', 'selectTable', 'bttable'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.selectType.init();
    bt.selectTable.init({
        elem: "#countryTable",
        url: basePath + "/basic/business/syscountry/jsonList",
        searchInput: ".text",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data, $tr) {
            $("input[name='sysId']").val(data.id);
            $("input[name='nameZh']").val(data.nameZh);
            $("input[name='nameEn']").val(data.nameEn);
            $("input[name='enCode']").val(data.isoTwo);
            $("input[name='code']").val(data.isoThree);
        },
    })

    //生成列表
    var rowMR = 0;
    bt.bttable.init({
        elem: "#dataTableArea",
        buttons: [{
            text: '<i class="layui-icon">&#xe642;</i>' + bt.lang('common.addTr'),
            action: function ($button) {
                rowMR++;
                var html = '<tr row=' + rowMR + '>';
                html += '<td align="center"><div class="layui-input-inline"><input type="text" id="nameZh' + rowMR + '" name="nameZhRow" autocomplete="off" class="layui-input" lay-verify="required|strlength" data-maxlength="100"></div></td>';
                html += '<td align="center"><div class="layui-input-inline"><input type="text" id="nameEn' + rowMR + '" name="nameEnRow" autocomplete="off" class="layui-input" lay-verify="required|strlength" data-maxlength="200"></div></td>';
                html += '<td align="center"><div class="layui-input-inline"><input type="text" id="code' + rowMR + '" name="codeRow" autocomplete="off" class="layui-input" lay-verify="required|strlength" data-maxlength="30"></div></td>';
                html += '<td align="center"><div class="layui-input-inline"><div class="bt-biz-selectType" data-url="'+basePath+'/basic/business/syscountrycust/getSysTimeZone" data-name ="timeZoneId' + rowMR + '" data-biz-name ="timeZoneIdRow" data-valueField="id" data-textField="timeZone" data-required = "true"></div></div></td>';
                html += '<td><a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a></td>';
                html += '</tr>';
                $("#dataTableArea").find("tbody").find("#sumTr").before(html);
                bt.selectType.init();
                form.render();
            }
        }],
        columns: [{
            data: "nameZh",
            render: function (data, row) {
                return '<input type="text" id="nameZh' + rowMR + '" name="nameZhRow" autocomplete="off" class="layui-input">';
            }
        }, {
            data: "nameEn",
            render: function (data, row) {
                return '<input type="text" id="nameEn' + rowMR + '" name="nameEnRow" autocomplete="off" class="layui-input">';
            }
        }, {
            data: "code",
            render: function (data, row) {
                return '<input type="text" id="code' + rowMR + '" name="codeRow" autocomplete="off" class="layui-input">';
            }
        }, {
            data: "timeZoneId",
            render: function (data, row) {
                return '<div class="bt-biz-selectType" data-url="${basePath}/basic/business/syscountrycust/getSysTimeZone" data-name ="timeZoneId' + rowMR + '" data-biz-name ="timeZoneIdRow" data-valueField="id" data-textField="timeZone"></div>';
            }
        }, {
            data: null,
            render: function (data, row) {
                return '<a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a>';
            }
        }],
        delRowCallback: function (data, $tr) {
            $tr.remove();
        }
    });

    //监听提交
    form.on('submit(submit)', function(data){
        var nameZhs = [];
        var nameEns = [];
        var codes = [];
        var timeZones = [];
        $('form').find('input[name="nameZhRow"]').each(function () {
            nameZhs.push($(this).val());
        });
        $('form').find('input[name="nameEnRow"]').each(function () {
            nameEns.push($(this).val());
        });
        $('form').find('input[name="codeRow"]').each(function () {
            codes.push($(this).val());
        });
        $('table#dataTableArea').find('select').each(function () {
            timeZones.push($(this).find("option:selected").val());
        });
        if (nameZhs) {
            data.field.nameZhs = nameZhs;
        }
        if (nameEns) {
            data.field.nameEns = nameEns;
        }
        if (codes) {
            data.field.codes = codes;
        }
        if (timeZones) {
            data.field.timeZones = timeZones;
        }
        var action = "/basic/business/syscountrycust/save";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.refreshParentTable();
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
});

// 重置
function countryCustReset() {
    window.location.reload();
}
