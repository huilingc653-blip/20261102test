var dicts, cell;
bt.use(['form', 'jquery', 'selectTree', 'corp', 'region', 'moneyType', 'dicts', 'selectType', 'cell', 'selectTable','checkboxs'], function () {
    var form = bt.form(), layer = bt.layer, $ = bt.jquery;
    bt.corp.init();
    var region = bt.region.init();
    bt.moneyType.init();
    bt.selectType.init();
    // bt.checkboxs.init();

    //国家地区下拉表格
    var countryInit = bt.selectTable.init({
        elem: "#countryTable",
        url: basePath + "/basic/business/syscountrycust/listSysCountryCust",
        searchInput: "#countryName",
        scrollX: "600px",
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data, $tr) {
            $("input[name='countryName']").val(data.nameZh);
            $("input[name='countryId']").val(data.id);
        },
    });

    var inputOperationareaSearchLayerIndex = null;
    $("#inputOperationareaBtn").click(function (){
        //  layerType  = 1 表示 编辑
        var url = basePath + "/sys/operationArea/selectPage"
        if ($("#newPage").val()){
            url += '?layerType=1'
        }

        inputOperationareaSearchLayerIndex = parent.layer.open({
            type: 2,
            area: ['850px', '620px'],
            maxmin: true,
            content: url,
        })
    })

    /**
     * 营运区维护查询选择回调
     * @param data
     * @param dom
     */
    inputOperationAreaCallback = function (selectedData) {
        // console.log(selectedData);
        var ids = '';
        var names = '';
        var provinceId = '';
        var provinceName = '';
        bt.each(selectedData,function (i,item){
            ids += item.operationAreaCode;
            names += item.operationAreaName;
            provinceId += item.provinceId;
            provinceName += item.provinceName;
            if(i < (selectedData.length -1)){
                ids += ',';
                names += ','
                provinceId += ',';
                provinceName += ',';
            }
        })
        $("#operationAreaId").val(ids);
        $("#operationAreaName").val(names);
        $("#provinceId").val(!provinceId || provinceId == 'null' ? "" : provinceId);
        $("#systemProvinceName").val(!provinceName || provinceName == 'null' ? "" : provinceName);
        inputOperationareaSearchLayerIndex && parent.layer.close(inputOperationareaSearchLayerIndex)
    }

    var inputUnitStoreSearchLayerIndex = null;
    $("#inputUnitStoreBtn").click(function (){
        //  layerType  = 1 表示 编辑
        var url = basePath + "/sys/unitStore/selectPage"
        if ($("#newPage").val()){
            url += '?layerType=1'
        }

        inputUnitStoreSearchLayerIndex = parent.layer.open({
            type: 2,
            area: ['850px', '620px'],
            maxmin: true,
            content: url,
        })
    })

    inputUnitStoreCallback = function (selectedData) {
        // console.log(selectedData);
        var ids = ''
        var names = ''
        bt.each(selectedData,function (i,item){
            ids += item.storeCode;
            names += item.storeName;
            if(i < (selectedData.length -1)){
                ids += ',';
                names += ','
            }
        })
        $("#unitStoreMaintenanceId").val(ids)
        $("#storeName").val(names)

        inputUnitStoreSearchLayerIndex && parent.layer.close(inputUnitStoreSearchLayerIndex)
    }

    // var inputUnitStore = bt.inputUnitStore.init({
    //     url: basePath + "/sys/unitStore/selectPage?layerType=1",
    // });



    //自定义验证规则
    form.verify({
        name: function (value) {
            if (value.length < 2) {
                // 组织名称至少得2个字符！
                return bt.lang('sys.sysCorp.minLength');
            }
            if (value.length > 200) {
                // 组织名称不能大于200个字符！
                return bt.lang('sys.sysCorp.maxLength');
            }
        },
        sortVerify: function (value) {
            var pattern = /^-?\d{0,5}$/;
            if (value && !pattern.test(value)) {
                // 排序值为正整数，并且不能大于5位数！
                return bt.lang('sys.sysCorp.sortVerify.maxLength');
            }
        },
        code: function (value) {
            if (value.length > 30) {
                // 机构代码不能大于30个字符！
                return bt.lang('sys.sysCorp.code.maxLength');
            }
            var pattern = /^[a-zA-Z0-9|-]*$/;
            if (!pattern.test(value)) {
                // 机构代码只能填写英文数字及'-'符号
                return bt.lang('sys.sysCorp.code.pattern');
            }
        },
        accountLicence: function (value) {
            if (value && value.length > 30) {
                // 开户许可证不能大于30个字符！
                return bt.lang('sys.sysCorp.accountLicence.maxLength');
            }
        },
        headPerson: function (value) {
            if (value && value.length > 30) {
                // 单位负责人不能大于30个字符！
                return bt.lang('sys.sysCorp.headPerson.maxLength');
            }
        },
        address: function (value) {
            if (value && value.length > 200) {
                // 单位地址不能大于200个字符！
                return bt.lang('sys.sysCorp.address.maxLength');
            }
        },
        socCode: function (value) {
            if (value && value.length > 32) {
                // 统一社会信用代码不能大于32个字符！
                return '统一社会信用代码不能大于32个字符！';
            }
        },
        certNo: function (value) {
            if (value && value.length > 60) {
                // 单位证件号码不能大于60个字符！
                return '单位证件号码不能大于60个字符！';
            }
        },
    });

    //监听提交
    form.on('submit(corpSubmit)', function (data) {
        if (typeof (data.field.status) == "undefined") {
            data.field.status = 0;
        } else {
            data.field.status = 1;
        }

        if (typeof (data.field.type) == "undefined") {
            data.field.type = 1;
        } else if (data.field.type === "on") {
            data.field.type = 0;
        } else {
            data.field.type = 1;
        }

        if (typeof (data.field.isLimitQuota) == "undefined") {
            data.field.isLimitQuota = 0;
        } else if (data.field.isLimitQuota === "on") {
            data.field.isLimitQuota = 1;
        } else {
            data.field.isLimitQuota = 0;
        }

        if (typeof (data.field.listedCompany) == "undefined") {
            data.field.listedCompany = 0;
        } else {
            data.field.listedCompany = 1;
        }

        var action = $('input[name="id"]').val() ? "update" : "save";
        bt.loading.show();
        $.post(basePath + '/sys/corp/' + action, data.field, function (result) {
            bt.loading.hide();
            if (result.success) {
                bt.navtab.refreshCurrent();
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        }, 'JSON');
        return false;
    });

    dicts = bt.dicts.init();
    cells = bt.cell.init();




    var inputOperationareaSearchLayerIndex = null;
    $("#inputProvinceIdBtn").click(function (){
        //  layerType  = 1 表示 编辑
        var url = basePath + "/sys/systemProvince/selectPage"
        if ($("#newPage").val()){
            url += '?layerType=1'
        }

        inputOperationareaSearchLayerIndex = parent.layer.open({
            type: 2,
            area: ['850px', '620px'],
            maxmin: true,
            content: url,
        })
    })

    /**
     * 省区维护查询选择回调
     * @param data
     * @param dom
     */
    inputSystemProvinceCallback = function (selectedData) {
        var ids = ''
        var names = ''
        bt.each(selectedData,function (i,item){
            ids += item.id
            names += item.provinceName
            if(i < (selectedData.length -1)){
                ids += ',';
                names += ','
            }
        })
        $("#provinceId").val(ids)
        $("#systemProvinceName").val(names)
        inputOperationareaSearchLayerIndex && parent.layer.close(inputOperationareaSearchLayerIndex)
    }

});

function parentCorpFun(data) {
    if (!cells.get("unitCmBiz").val()) {
        cells.get("unitCmBiz").val(data.unitCm);
    }
}

function unitAttributeFun(data, text, selectedData, beforeValue) {
    if (selectedData && selectedData.value == '02') {
        $('[data-biz-name="unitCmBiz"]').attr("lay-verify", "required");
    } else {
        $('[data-biz-name="unitCmBiz"]').removeAttr("lay-verify");
    }
    bt.form().renderIcon();
}


function divclick() {
    if ($("#otherInfo").is(":hidden")) {
        $("#otherInfo").show();    //如果元素为隐藏,则将它显现
        $("#otherInfoIcon").html("&#xe61a;")
    } else {
        $("#otherInfo").hide();     //如果元素为显现,则将其隐藏
        $("#otherInfoIcon").html("&#xe602;")
    }
}
