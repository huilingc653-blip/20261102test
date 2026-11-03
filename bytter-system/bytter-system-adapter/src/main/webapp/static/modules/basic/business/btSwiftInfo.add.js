bt.use(['form', 'layedit', 'selectTable', 'dicts','selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.dicts.init();
    //自定义验证规则（需在提交前注册）
    form.verify({
        swiftCode: function(value){
            if (!/^[A-Z0-9]{8}$|^[A-Z0-9]{11}$/.test(value)) {
                return bt.lang('table.btswiftinfo.swiftCode.check');
            }
        },
        // 机构名称：仅英文大小写字母、英文标点、数字（含空格）
        bankName: function(value){
            if (value && !/^[A-Za-z0-9\s.,!?;:'"()\-\/&#@]+$/.test(value)) {
                return '机构名称只能输入英文大小写字母、英文标点和数字';
            }
        }
    });

    /**初始化国家下拉表格，组件：selectTable;    begin*/
    var countryInit = bt.selectTable.init({
        elem: "#countryCode",
        url: basePath + "/basic/business/syscountrycust/listSysCountryCust",
        searchInput: "#nameZh",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data, $tr) {
            $("input[name='countryCode']").val(data.id);
            $("input[name='nameZh']").val(data.nameZh);
            $("#cityNameZh").val("");
            cityInit.get("cityNameZh").rebuild({
                url: basePath + "/basic/business/syscountrycust/listSysCountryArea/"+$("input[name='countryCode']").val(),
                columns: [
                    {data: "code",},
                    {data: "nameZh",},
                    {data: "nameEn",},
                ]
            });
        },
    })
    /**初始化国家下拉表格 end*/

    /**初始化地区下拉表格，组件：selectTable;    begin*/
    var countryCode = "0";
    if($("input[name='countryCode']").val()){
        countryCode = $("input[name='countryCode']").val();
    }
    var cityInit = bt.selectTable.init({
        elem: "#cityNameTable",
        url: basePath + "/basic/business/syscountrycust/listSysCountryArea/"+countryCode,
        searchInput: "#cityNameZh",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data, $tr) {
            $("input[name='cityName']").val(data.id);
            $("input[name='cityNameZh']").val(data.nameZh);
        },
    })
    /**初始化地区下拉表格 end*/

    /**表单保存 begin*/
    form.on('submit(submit)', function (data) {
        var action = "/basic/business/btswiftinfo/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshParent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });
    /**表单保存 end*/
});
