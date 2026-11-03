bt.biz = function (bizName, pluginName) {
    return window.biz[bizName][pluginName];
}

var form;
var selectTypes;
var cacheQuickyPayInfo = {}; // 缓存数据
bt.use(['form', 'jquery', 'corp','select','selectType', 'bttable', 'bankInfo', 'region', 'dicts', 'externalCorpBankAcc','btUpload','selectTableType','swiftInfo','mulselect'], function () {
    form = bt.form(), $ = bt.jquery;
    var layer = parent ? parent.bt.layer : bt.layer;
    //数据来源，用到字典
    dicts = bt.dicts.init();
    var corp = bt.corp.init();
    var externalCorpBankAcc = bt.externalCorpBankAcc.init();
    var region = bt.region.init();
    selectTypes = bt.selectType.init();//下拉框
    select=bt.selectType.init();
    var bankInfo = bt.bankInfo.init();
    mulselect = bt.mulselect.init();
    var uploader = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });

    // $.get(basePath + "/sys/externalCorp/getQuickPayInfo?externalCorpId="+$('#externalCorpId').val(), function(result){
    //    console.log(result)
    // },'JSON');

    var swiftInfo = bt.swiftInfo.init();
    var bankAccTable;
    //debugger;
    //自定义验证规则
    var englishOnlyReg = /^(?=.*[a-zA-Z])[a-zA-Z0-9\s.,!?;:'"()-]+$/;
    var englishLetterPunctuationReg = /^(?=.*[a-zA-Z])[a-zA-Z\s\x21-\x2F\x3A-\x40\x5B-\x60\x7B-\x7E]+$/;
    var alphaNumericReg = /^[a-zA-Z0-9]+$/;
    form.verify({
        name: function(value){
            if(value.length < 2){
                // 客户名称至少得2个字符！
                return bt.lang('sys.externalCorp.name.mixLength');
            }
            if(value.length>100){
                // 客户名称不能大于100个字符！
                return bt.lang('sys.externalCorp.name.maxLength');
            }
        },
        abbreviate: function(value){
            if(value.length>100){
                // 客户简称不能大于100个字符！
                return bt.lang('sys.externalCorp.maxLength');
            }
        },

        address: function(value){
            if(value.length>50){
                // 地址不能大于50个字符！
                return bt.lang('sys.externalCorp.address.maxLength');
            }
        },
        linkMan: function(value){
            if(value.length>5){
                // 联系人不能大于5个字符！
                return bt.lang('sys.externalCorp.linkMan.maxLength');
            }
        },
        tel: function(value){
            if(value.length>15){
                // 联系电话不能大于15个字符！
                return bt.lang('sys.externalCorp.tel.maxLength');
            }
        },
        fax: function(value){
            if(value.length>15){
                // 传真不能大于15个字符！
                return bt.lang('sys.externalCorp.fax.maxLength');
            }
        },
        email: function(value){
            if(value.length>30){
                // email不能大于30个字符！
                return bt.lang('sys.externalCorp.email.maxLength');
            }
            if (value && !/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(value)) {
                return bt.lang('sys.form.verify.emailError')//'邮箱格式不正确';
            }
        },
        alphaNumeric: function(value) {
            if (value !="" && !alphaNumericReg.test(value)) {
                return bt.lang('sys.externalCorp.alphaNumeric');
            }
        },
        englishOnly: function(value, item) {
            if ($.trim(value) != "" && !englishOnlyReg.test(value)) {
                var langKeyMap = {
                    provinceState: 'sys.externalCorp.provinceState.englishOnly',
                    address1: 'sys.externalCorp.address1.englishOnly',
                    address2: 'sys.externalCorp.address2.englishOnly'
                };
                return bt.lang(langKeyMap[$(item).attr('name')] || 'sys.externalCorp.englishOnly');
            }
        },
        nameEnEnglishOnly: function(value) {
            if ($.trim(value) != "" && !englishLetterPunctuationReg.test(value)) {
                return bt.lang('sys.externalCorp.nameEn.englishOnly');
            }
        },
        addressEnEnglishOnly: function(value) {
            if ($.trim(value) != "" && !englishLetterPunctuationReg.test(value)) {
                return bt.lang('sys.externalCorp.addressEn.englishOnly');
            }
        }

    });
    /*form.on('switch(checkCorp)', function(data){
        if (!this.checked) {
            //所属单位
            $("#inputCorpName").html(bt.lang('table.externalCorp.corpName'));

            $("#inputCorpDiv1").show();
            $("#inputCorpDiv1").find('.select-tree-input').attr("lay-verify","required");
            $("#inputCorpDiv1").find('.select-tree-input').removeAttr("disabled");

            $("#inputCorpDiv2").hide();
            $("#inputCorpDiv2").find('.select-tree-input').removeAttr("lay-verify");
            $("#inputCorpDiv2").find('.select-tree-input').attr("disabled","disabled");
        }else{
            //共享范围
            $("#inputCorpName").html(bt.lang('table.btPurpose.corpId'));

            $("#inputCorpDiv1").hide();
            $("#inputCorpDiv1").find('.select-tree-input').removeAttr("lay-verify");
            $("#inputCorpDiv1").find('.select-tree-input').attr("disabled","disabled");

            $("#inputCorpDiv2").show();
            $("#inputCorpDiv2").find('.select-tree-input').attr("lay-verify","required");
            $("#inputCorpDiv2").find('.select-tree-input').removeAttr("disabled");
        }
        form.renderIcon();
    });*/

    if ($("#isCommonValue").val() !== "") {
        initCorpAndisCommon($("#isCommonValue").val());
    }
    initSupplier($("#supplierIdHidden").val());

    form.on('submit(submitEvent)', function(data) {
        data.field.auditStatus = 11;
        return submitForm(data);
    });

    form.on('submit(storageEvent)', function(data) {
        data.field.auditStatus = 0;
        return submitForm(data);
    });

    function convertToDotNotation(arr,fields) {
        $.each(arr, function(index, obj) {
            for (var key in obj) {
                var key1 = 'quickPayList['+index+'].'+key;
                if(key =='isDefaultAcc'){
                    if(obj[key] == true){
                        obj[key] = 'on';
                    }else{
                        obj[key] = 'off';
                    }
                }
                fields[key1] = obj[key];
            }
        });
    }

    // 提交前兜底校验动态显示字段，避免境内/境外切换后 lay-verify 漏校验。
    function verifyExternalCorpFields() {
        var verifyItems = [
            {selector: '[name="nameEn"]', reg: englishLetterPunctuationReg, msg: bt.lang('sys.externalCorp.nameEn.englishOnly')},
            {selector: '[name="provinceState"]', reg: englishOnlyReg, msg: bt.lang('sys.externalCorp.provinceState.englishOnly')},
            {selector: '[name="address1"]', reg: englishOnlyReg, msg: bt.lang('sys.externalCorp.address1.englishOnly')},
            {selector: '[name="address2"]', reg: englishOnlyReg, msg: bt.lang('sys.externalCorp.address2.englishOnly')},
            {selector: '[name="addressEn"]', reg: englishLetterPunctuationReg, msg: bt.lang('sys.externalCorp.addressEn.englishOnly')},
            {selector: '[name="zipCode"]', reg: alphaNumericReg, msg: bt.lang('sys.externalCorp.alphaNumeric')},
            {selector: '[name="fax"]', maxLength: 15, msg: bt.lang('sys.externalCorp.fax.maxLength')}
        ];
        for (var i = 0; i < verifyItems.length; i++) {
            var verifyItem = verifyItems[i];
            var input = $(verifyItem.selector);
            if (!input.length || input.attr('disabled') || !input.is(':visible')) {
                continue;
            }
            var value = $.trim(input.val());
            if (verifyItem.maxLength && value.length > verifyItem.maxLength) {
                layer.msg(verifyItem.msg, {icon: 5, shift: 6});
                input.addClass('layui-form-danger');
                input.focus();
                return false;
            }
            if (verifyItem.reg && value !== '' && !verifyItem.reg.test(value)) {
                layer.msg(verifyItem.msg, {icon: 5, shift: 6});
                input.addClass('layui-form-danger');
                input.focus();
                return false;
            }
        }
        return true;
    }

    function  submitForm(data) {
        if (!verifyExternalCorpFields()) {
            return false;
        }

        //判断账号重复
        /*var accArr = $(".externalAcc");
        var nary = accArr.sort();
        var hash = {};
        for(var i = 0; i < nary.length; i++) {
            if(hash[nary[i].value]){
                layer.alert(bt.lang("sys.externalcorpbankacc.acc.same"));
                return false;
            }
            hash[nary[i].value] = true;
        }*/


        var bankAccTrs = $("#bankAcc").find('tbody tr');
        var arr =[];
        var flag = true;
        bt.each(bankAccTrs,function () {
            var rowIndex = $(this).attr('row');
            var key1 = 'externalAcc['+rowIndex+'].externalAcc'
            var key2 = 'externalAcc['+rowIndex+'].curId'
            var values = data.field[key1] + data.field[key2];
            if(arr.indexOf(values) === -1){
                arr.push(values);
            }else{

                flag = false;
                return false;
            }
        })

        if(!flag){
            layer.alert(bt.lang("sys.externalcorpbankacc.acc.same"));
            return false;
        }

        //是否有效
        if (typeof(data.field.status) == "undefined") {
            data.field.status = 0;
        } else if (data.field.status === "on") {
            data.field.status = 1;
        } else {
            data.field.status = 0;
        }
        //是否国内
        if (typeof(data.field.isNative) == "undefined") {
            data.field.isNative = 0;
            data.field.province = $('#province').val();
            data.field.city = $('#city').val();
        } else if (data.field.isNative === "on") {
            data.field.isNative = 1;
            data.field.province = $('[name="province"] option:checked').val();
            data.field.city = $('[name="city"] option:checked').val();
        } else {
            data.field.isNative = 0;
            data.field.province = $('#province').val();
            data.field.city = $('#city').val();
        }
        // 组装快捷支付信息数据据
        data.field.quickPayList = [];
        for (var key in cacheQuickyPayInfo){
            data.field.quickPayList =  data.field.quickPayList.concat(cacheQuickyPayInfo[key])
        }
        convertToDotNotation(data.field.quickPayList,data.field);
        if(data.field.isNative == 1){
            data.field.address = $('#internalAddress').val();
        }else{
            data.field.address = $('#externalAddress').val();
        }

        delete data.field.quickPayList
        delete data.field.proId

        if(data.field.isNative == 0){
            var lastTr =$('#qpiBody').find('tr:last-child');
            if(lastTr[0]){
                var proSelectValue = $.trim(lastTr.find('.type-select select').val());
                if(!proSelectValue || !cacheQuickyPayInfo[proSelectValue] || !(cacheQuickyPayInfo[proSelectValue] && cacheQuickyPayInfo[proSelectValue].length)){
                    parent.layer.msg(bt.lang('table.externalCorpQuickPay.quickPayInfo.edit'));
                    return false;
                }
            }
        }
        var action = $('input[name="id"]').val() ? "update" : "save";
        data.field.attachmentId = uploader.getGroupId();
        $.post(basePath + '/sys/externalCorp/' + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2},function () {
                layer.closeAll();
                if (result.success) {
                    if(!bt.navtab){
                        parent.layer?parent.layer.closeAll():layer.closeAll();
                    }else{
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshParent();
                    }
                }
            });
        }, 'JSON');
        return false;
    }

    var selectTable = bt.selectTableType.init({
        elem: ".province",
        paging: true,
        url: basePath + "/basic/business/syscountrycust/listSysCountryCust",
        searchInput: "#provinceTemp",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "code",title: bt.lang("table.tbBaseRate.code")},
            {data: "nameZh",title: bt.lang("table.btswiftinfo.namezh")},
            {data: "nameEn",title: bt.lang("table.btswiftinfo.nameen")},
        ],
        clickCallback: function (data, tr, elem, full) {
            elem.val(data.nameZh);
            $("#province").val(data.id);
            $("#cityTemp").val('').data('value', '').data('beforevalue', '');
            $("#city").val('');
            cityInit.get("cityTemp").rebuild({
                url: basePath + "/basic/business/syscountrycust/listSysCountryArea/"+data.id,
                columns: [
                    {data: "code",},
                    {data: "nameZh",},
                    {data: "nameEn",},
                ]
            });
        }
    });

    var province = "0";
    if($("#province").val()){
        province = $("#province").val();
    }
    var cityInit = bt.selectTableType.init({
        elem: ".city",
        paging: true,
        url: basePath + "/basic/business/syscountrycust/listSysCountryArea/"+province,
        searchInput: "#cityTemp",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "code",title: bt.lang("table.tbBaseRate.code")},
            {data: "nameZh",title: bt.lang("table.btswiftinfo.namezh")},
            {data: "nameEn",title: bt.lang("table.btswiftinfo.nameen")},
        ],
        clickCallback: function (data, tr, elem, full) {
            elem.val(data.nameZh);
            $("#city").val(data.id);
        }
    });

    bt.bankinfoCallback = function (data, dom) {
        var tr = dom.parents('tr');
        var index = tr.attr("row");
        tr.find('td:eq(2) textarea').val(data.bankName);
        tr.find('td:eq(3) input').val(data.bankCode);
        $("#externalAccBankType" + index).val(data.bankTypeId);
        $("#swiftCode" + index).val(data.swiftCode);

        region.get('externalRegion' + index).set([{
            name: 'externalAcc[' + index + '].accProvince',
            defaultValue: data.prov
        }, {
            name: 'externalAcc[' + index + '].accCity',
            defaultValue: data.city
        }]);
    }

    //点击swift后回调
    swiftInfoCallback=function(data,dom){
        var tr = dom.parents('tr');
        var tds = dom.parents('tr').children('td');
        var index = tr.attr("row");
        tds.eq(3).find('textarea').val(data.bankName);
        tds.eq(4).find('input').val(data.swiftCode);
        $("#localClearNo" + index).val(data.localClearNo);


    }

    //点击swift后回调
    intermediaryBankSwiftInfoCallback=function(data,dom){
        var tr = dom.parents('tr');
        var index = tr.attr("row");
        $("#intermediaryBank" + index).val(data.bankName);
        $("#intermediaryBankSwiftCode" + index).val(data.swiftCode);
        $("#intermediaryBankAddress" + index).val(data.bankAdd);

    }

    var extenrnalId = $('input[name="id"]').val();
    var rowTr = 0;

    //监听是否国内
    form.on('switch(checkNative)', function(data){
        $(".is_city").toggleClass('hide');
        $(".addressEn").toggleClass('hide');
        $(".address").toggleClass('hide');
        $(".zipCode").toggleClass('hide');
        $(".quicklyPayInfo").toggleClass('hide');
        $("#bankAcc").find('tbody').html('');
        rowTr = 0;
        if (!this.checked) {
            $("#provinceTemp").attr("lay-verify","required");
            $("#cityTemp").attr("lay-verify","required");
            $("#addressTemp").attr("lay-verify","address");
            region.get('externalRegion').set([{
                name: 'province',
                defaultValue: ''
            }, {
                name: 'city',
                defaultValue: ''
            }]);
        }else{
            $("#provinceTemp").removeAttr("lay-verify","required");
            $("#cityTemp").removeAttr("lay-verify","required");
            $("#addressTemp").removeAttr("lay-verify","address");
        }
        $("#city").val('');
        form.renderIcon();
    });

    /*form.on('switch(defaultAcc)', function (data) {
        var elem = data.elem; // 获得 checkbox 原始 DOM 对象
        var checked = elem.checked; // 获得 checkbox 选中状态
        if(checked){
            var siblingsTrs = data.othis.closest('tr').siblings('tr');
            if(siblingsTrs[0]){
                bt.each(siblingsTrs.find('[name="isDefaultAcc"]'),function () {
                    this.checked = false;
                })
                form.render('checkbox');
            }
        }

    });*/

    var addRow = function(index){
        var isCity = !$('#bankAccY').hasClass('hide');
        isCity ? addBankAcc(index):addBankAccFex(index);
    }

    //国内账号表格新增行
    addBankAcc = function (index) {
        var html = '<tr row="' + index + '">';
        html += '<td><input type="text" name="externalAcc[' + index + '].externalAcc" lay-verify="required|strlength" autocomplete="off" class="layui-input externalAcc" data-maxlength="30"/></td>';
        html += '<td><div class="layui-inline"><div class="bt-biz-selectType" data-name="externalAcc[' + index + '].curId" data-url= "'+ basePath +'/basic/business/btcurrency/json" data-method="get" data-valuefield="id" data-textfield="englishCode"  data-required="true" data-biz-name="externalAcc[' + index + '].curId" data-defaultvalue="4028802e5c346968015c346c18850008" data-width="100px"></div></div></td>';
        html += '<td><input type="hidden" name="externalAcc[' + index + '].externalCorpId" value="' + extenrnalId + '" /><input type="hidden" name="externalAcc[' + index + '].bankType" id="externalAccBankType' + index+ '"  /><textarea name="externalAcc[' + index + '].bank" readonly class="layui-textarea"></textarea></td>';
        html += '<td><div class="layui-input-inline"><input type="text" name="externalAcc[' + index + '].bankSourceCode" autocomplete="off" lay-verify="required" class="layui-input" /><div class="bt-div-search"><button class="bt-biz-bankInfo"  type="button" data-callback="bt.bankinfoCallback"><i class="layui-icon">&#xe615;</i></button></div></div></td>';
        html += '<td><div class="bt-biz-region" data-group="externalRegion' + index + '" data-name="externalAcc[' + index + '].accProvince" data-change="externalAcc[' + index + '].accCity" data-label="' + bt.lang("common.placeholder.select") + '" data-required="true"></div></td>';
        html += '<td><div class="bt-biz-region" data-group="externalRegion' + index + '" data-name="externalAcc[' + index + '].accCity" data-label="' + bt.lang("common.placeholder.select") + '" data-required="true"></div></td>';
        html += '<td width="8%"><div class="bt-biz-dict" id = "abBillBlIdTpcd" data-type="select" data-group="AB_BILL_BL_ID_TPCD" data-name="externalAcc[' + index + '].identifyType" data-label="' + bt.lang("common.placeholder.select") + '" data-defaultvalue = "DT02" data-required="true" data-width="100px"></div></td>';
        html += '<td><div class="layui-input-inline"><input type="text" name="externalAcc[' + index + '].swiftCode" id="swiftCode' + index+ '" autocomplete="off" class="layui-input" /></div></td>';
        html += '<td><textarea class="layui-textarea" name="externalAcc[' + index + '].remark"></textarea></td>';
        html += '<td style="text-align: center;"><input type="checkbox" class="defaultAcc" name="externalAcc[' + index + '].isDefaultAcc" lay-skin="switch" lay-filter="defaultAcc" lay-text="' + bt.lang("common.sc_yn.yes") + '|' + bt.lang("common.sc_yn.no") + '"></td>';
        // if(index !== 0){
        html += '<td><a href="javascript:void(0);" class="delete-bankAcc">' + bt.lang("common.button.delete") + '</a></td>';
        // }else {
        //     html += '<td></td>';
        // }
        html += '</tr>';
        $("#bankAcc").append(html);
        form.render();
        region.render();
        dicts.render();
        select.render();
        bt.selectType.init();
    }

    //国外帐号表格新增行
    addBankAccFex = function (index) {
        var html = '<tr row="' + index + '">';
        html += '<td><input type="text" name="externalAcc[' + index + '].externalAcc" lay-verify="required|strlength" autocomplete="off" class="layui-input externalAcc" data-maxlength="30"/></td>';
        html += '<td><input type="text" name="externalAcc[' + index + '].externalAccName" lay-verify="required|strlength" autocomplete="off" class="layui-input externalAcc" data-maxlength="100"/></td>';
        html += '<td><div class="layui-inline"><div class="bt-biz-selectType" data-name="externalAcc[' + index + '].curId" data-url= "'+ basePath +'/basic/business/btcurrency/json" data-method="get" data-valuefield="id" data-textfield="englishCode"  data-required="true" data-biz-name="externalAcc[' + index + '].curId" data-defaultvalue="4028802e5c346968015c346c18850008" data-width="100px"></div></div></td>';
        html += '<td><input type="hidden" name="externalAcc[' + index + '].externalCorpId" value="' + extenrnalId + '" /><textarea name="externalAcc[' + index + '].bank" readonly class="layui-textarea"></textarea></td>';
        html += '<td><div class="layui-input-inline"><input type="text" name="externalAcc[' + index + '].swiftCode" autocomplete="off" lay-verify="required" class="layui-input"><div class="bt-div-search"><button class="bt-biz-swiftInfo" type="button" data-callback="swiftInfoCallback"><i class="layui-icon">&#xe615;</i></button></div></div></td>';
        html += '<td><input type="text" name="externalAcc[' + index + '].localClearNo" lay-verify="strlength" id="localClearNo' + index+ '" autocomplete="off" class="layui-input externalAcc" data-maxlength="100"/></td>';
        html += '<td><div class="bt-biz-dict" data-type="select" data-group="sysExternalCorpBankAcc.payExcType" data-name="externalAcc[' + index + '].payExcType" data-label="' + bt.lang("table.externalBankAcc.payExcType") + '" data-width="120px"></div></td>';
        html += '<td><input type="text" name="externalAcc[' + index + '].intermediaryBank" id="intermediaryBank' + index+ '" lay-verify="strlength" autocomplete="off" class="layui-input externalAcc" data-maxlength="100"/></td>';
        html += '<td><div class="layui-input-inline"><input type="text" name="externalAcc[' + index + '].intermediaryBankSwiftCode" id="intermediaryBankSwiftCode' + index + '" autocomplete="off" class="layui-input"><div class="bt-div-search"><button class="bt-biz-swiftInfo" type="button" data-callback="intermediaryBankSwiftInfoCallback"><i class="layui-icon">&#xe615;</i></button></div></div></td>';
        html += '<td><input type="text" name="externalAcc[' + index + '].intermediaryBankAddress" id="intermediaryBankAddress' + index+ '" lay-verify="strlength" autocomplete="off" class="layui-input externalAcc" data-maxlength="200"/></td>';
        html += '<td><input type="text" name="externalAcc[' + index + '].intermediaryBankAcc" id="intermediaryBankAcc' + index+ '" lay-verify="strlength" autocomplete="off" class="layui-input externalAcc" data-maxlength="100"/></td>';
        html += '<td><textarea class="layui-textarea" name="externalAcc[' + index + '].remark"></textarea></td>';
        html += '<td style="text-align: center;"><input type="checkbox" class="defaultAcc" name="externalAcc[' + index + '].isDefaultAcc" lay-skin="switch" lay-filter="defaultAcc" lay-text="' + bt.lang("common.sc_yn.yes") + '|' + bt.lang("common.sc_yn.no") + '"></div></td>';
        // if(index !== 0){
        html += '<td><a href="javascript:void(0);" class="delete-bankAcc">' + bt.lang("common.button.delete") + '</a></td>';
        // }else {
        //     html += '<td></td>';
        // }
        html += '</tr>';
        $("#bankAcc").append(html);
        form.render();
        bt.selectType.init();
        dicts.render();
    }

    var columnsTab = function() {
        var isCity = !$('#bankAccY').hasClass('hide');
        var columns = "";
        if(isCity){
            columns = [
                {
                    data: "externalAcc",
                    render: function (data, row) {
                        return '<input type="text" name="externalAcc[' + row + '].externalAcc"  lay-verify="required|strlength" value="' + data.externalAcc + '" autocomplete="off" class="externalAcc layui-input"  data-maxlength="30"/>';
                    }
                },
                {
                    data: "curId",
                    render: function (data, row) {
                        return '<div class="layui-inline"><div class="bt-biz-selectType" data-name="externalAcc[' + row + '].curId" data-url= "'+ basePath +'/basic/business/btcurrency/json" data-method="get" data-valuefield="id" data-textfield="englishCode"  data-required="true" data-biz-name="externalAcc[' + row + '].curId" data-defaultvalue="' + data.curId + '"></div></div>';
                    }
                },
                {
                    data: "externalCorpId",
                    render: function (data, row) {
                        return '<input type="hidden" name="externalAcc[' + row + '].externalCorpId" value="' + data.externalCorpId + '" /><input type="hidden" name="externalAcc[' + row + '].bankType" id="externalAccBankType' + row + '"  /><textarea name="externalAcc[' + row + '].bank" class="layui-textarea">' + data.bank + '</textarea>';
                    }
                },
                {
                    data: "bankSourceCode",
                    render: function (data, row) {
                        return '<div class="layui-input-inline"><input type="text" name="externalAcc[' + row + '].bankSourceCode" value="' + data.bankSourceCode + '" autocomplete="off" class="layui-input"/><div class="bt-div-search"><button class="bt-biz-bankInfo"  type="button" data-callback="bt.bankinfoCallback"><i class="layui-icon">&#xe615;</i></button></div></div>';
                    }
                },
                {
                    data: 'province',
                    render: function (data, row) {
                        return '<div class="bt-biz-region" data-group="externalRegion' + row + '" data-name="externalAcc[' + row + '].accProvince" data-change="externalAcc[' + row + '].accCity" data-label="' + bt.lang("common.placeholder.select") + '" data-defaultValue="' + data.accProvince + '" data-required="true"></div>';
                    }
                },
                {
                    data: 'city',
                    render: function (data, row) {
                        return '<div class="bt-biz-region" data-group="externalRegion' + row + '" data-name="externalAcc[' + row + '].accCity" data-label="' + bt.lang("common.placeholder.select") + '" data-defaultValue="' + data.accCity + '" data-required="true"></div>';
                    }
                },
                {
                    data: 'identifyType',
                    render: function (data, row) {
                        return '<div class="bt-biz-dict" data-type="select" data-group="AB_BILL_BL_ID_TPCD" data-name="externalAcc[' + row + '].identifyType" data-label="' + bt.lang("common.placeholder.select") + '" data-defaultValue="' + data.identifyType + '"></div>';
                    }
                },
                {
                    data: 'swiftCode',
                    render: function (data, row) {
                        return '<input type="text" name="externalAcc[' + row + '].swiftCode"  lay-verify="strlength" value="' + (data.swiftCode ? data.swiftCode : "") + '" autocomplete="off" class="layui-input" data-maxlength="30" />';
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
                        // if(row === 0){
                        //     return '';
                        // }else {
                        return '<a href="javascript:void(0);" class="delete-bankAcc">' + bt.lang("common.button.delete") + '</a>';
                        // }
                    }
                }
            ];
        }else{
            columns = [
                {
                    data: "externalAcc",
                    render: function (data, row) {
                        return '<input type="text" name="externalAcc[' + row + '].externalAcc"  lay-verify="required|strlength" value="' + data.externalAcc + '" autocomplete="off" class="externalAcc layui-input" data-maxlength="30" />';
                    }
                },
                {
                    data: "externalAccName",
                    render: function (data, row) {
                        return '<input type="text" name="externalAcc[' + row + '].externalAccName"  lay-verify="required|strlength" value="' + data.externalAccName + '" autocomplete="off" class="externalAcc layui-input" data-maxlength="100" />';
                    }
                },
                {
                    data: "curId",
                    render: function (data, row) {
                        return '<div class="layui-inline"><div class="bt-biz-selectType" data-name="externalAcc[' + row + '].curId" data-url= "'+ basePath +'/basic/business/btcurrency/json" data-method="get" data-valuefield="id" data-textfield="englishCode"  data-required="true" data-biz-name="externalAcc[' + row + '].curId" data-defaultvalue="' + data.curId + '"></div></div>';
                    }
                },
                {
                    data: "externalCorpId",
                    render: function (data, row) {
                        return '<input type="hidden" name="externalAcc[' + row + '].externalCorpId" value="' + data.externalCorpId + '" /><textarea name="externalAcc[' + row + '].bank" class="layui-textarea">' + data.bank + '</textarea>';
                    }
                },
                {
                    data: "swiftCode",
                    render: function (data, row) {
                        return '<div class="layui-input-inline"><input type="text" name="externalAcc[' + row + '].swiftCode" autocomplete="off" lay-verify="required" class="layui-input" value="' + data.swiftCode + '"><div class="bt-div-search"><button class="bt-biz-swiftInfo" type="button" data-callback="swiftInfoCallback"><i class="layui-icon">&#xe615;</i></button></div></div>';
                    }
                },
                {
                    data: "localClearNo",
                    render: function (data, row) {
                        return '<input type="text" name="externalAcc[' + row + '].localClearNo"  id="localClearNo' + row+ '" lay-verify="strlength" value="' + (data.localClearNo ? data.localClearNo : "") + '" autocomplete="off" class="externalAcc layui-input" data-maxlength="100" />';
                    }
                },
                {
                    data: 'payExcType',
                    render: function (data, row) {
                        return '<div class="bt-biz-dict" data-type="select" data-group="sysExternalCorpBankAcc.payExcType" data-name="externalAcc[' + row + '].payExcType" data-label="' + bt.lang("table.externalBankAcc.payExcType") + '" data-defaultValue="' + data.payExcType + '" data-width="120px"></div>';
                    }
                },
                {
                    data: 'intermediaryBank',
                    render: function (data, row) {
                        return '<input type="text" name="externalAcc[' + row + '].intermediaryBank" id="intermediaryBank' + row + '" value="'+ (data.intermediaryBank ? data.intermediaryBank : "") +'" lay-verify="strlength" autocomplete="off" class="layui-input externalAcc" data-maxlength="100"/></td>';
                    }
                },
                {
                    data: 'intermediaryBankSwiftCode',
                    render: function (data, row) {
                        return '<div class="layui-input-inline"><input type="text" name="externalAcc[' + row + '].intermediaryBankSwiftCode" id="intermediaryBankSwiftCode' + row + '" value="' + (data.intermediaryBankSwiftCode ? data.intermediaryBankSwiftCode:"") + '" autocomplete="off" class="layui-input"><div class="bt-div-search"><button class="bt-biz-swiftInfo" type="button" data-callback="intermediaryBankSwiftInfoCallback"><i class="layui-icon">&#xe615;</i></button></div></div>';
                    }
                },
                {
                    data: 'intermediaryBankAddress',
                    render: function (data, row) {
                        return '<input type="text" name="externalAcc[' + row + '].intermediaryBankAddress" id="intermediaryBankAddress' + row+ '" value="'+ (data.intermediaryBankAddress ? data.intermediaryBankAddress:"") +'" lay-verify="strlength" autocomplete="off" class="layui-input externalAcc" data-maxlength="200"/>';
                    }
                },
                {
                    data: 'intermediaryBankAcc',
                    render: function (data, row) {
                        return '<input type="text" name="externalAcc[' + row + '].intermediaryBankAcc" id="intermediaryBankAcc' + row+ '" value="' + (data.intermediaryBankAcc ? data.intermediaryBankAcc:"") + '" lay-verify="strlength" autocomplete="off" class="layui-input externalAcc" data-maxlength="100"/>';
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
                        // if(row === 0){
                        //     return '';
                        // }else {
                        return '<a href="javascript:void(0);" class="delete-bankAcc">' + bt.lang("common.button.delete") + '</a>';
                        // }
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
                    rowTr ++;
                }
            }
        ],
        columns: columnsTab(),
        delRowCallback: function (data, $tr) {
            $tr.remove();
        },
        tableInitCallback: function (data) {
            rowTr = data.length;
            form.render();
            region.render();
            dicts.render();
            select.render();
        }
    });

    $(document).on('click', ".delete-bankAcc", function () {
        var this_ = $(this)
        parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
            var tr = this_.parents('tr');
            var index = tr.attr("row");
            $('input[name="externalAcc[' + index + '].externalAcc"]').attr("value","");
            tr.remove();
        });
    });

    function initCorpAndisCommon(isCommon) {
        if (isCommon === "0"){
            $("#inputCorpName").html(bt.lang('table.externalCorp.corpName'));

            $("#inputCorpDiv1").show();
            $("#inputCorpDiv1").find('.select-tree-input').attr("lay-verify","required");
            $("#inputCorpDiv1").find('.select-tree-input').removeAttr("disabled");

            $("#inputCorpDiv2").hide();
            $("#inputCorpDiv2").find('.select-tree-input').removeAttr("lay-verify");
            $("#inputCorpDiv2").find('.select-tree-input').attr("disabled","disabled");
            $("input[name='isCommon']").prop("checked",false);
        }else if (isCommon === "1"){
            $("#inputCorpName").html(bt.lang('table.btPurpose.corpId'));

            $("#inputCorpDiv1").hide();
            $("#inputCorpDiv1").find('.select-tree-input').removeAttr("lay-verify");
            $("#inputCorpDiv1").find('.select-tree-input').attr("disabled","disabled");

            $("#inputCorpDiv2").show();
            $("#inputCorpDiv2").find('.select-tree-input').attr("lay-verify","required");
            $("#inputCorpDiv2").find('.select-tree-input').removeAttr("disabled");
            $("input[name='isCommon']").prop("checked",true);
        }
        form.renderIcon();
    }

    /**
     * 控制首选账户只能选中一个
     */
    form.on('switch(defaultAcc)', function (data) {
        var tr = $(data.othis).parents("tr");
        var index = tr.index();
        $.each($(".defaultAcc"), function (i, item) {
            if (i !== index) {
                $(this).prop("checked", false);
            }
        });
        form.render("checkbox");
    });

    /**
     * 快捷支付信息
     * */
    var isCanAddProId = true
    bt.initProductList = function(data){
        var newData = [];
        var trs = $('#qpiBody').find('tr');
        var selectedProIds = []
        bt.each(trs,function (i,item) {
            var value = $.trim($(item).find('.type-select select').val());
            if(value){
                selectedProIds.push(value);
            }
        })
        if(!selectedProIds.length){
            return  data;
        }
        bt.each(data,function (i,item) {
            if(item.id && (selectedProIds.indexOf(item.id) === -1)){
                newData.push(item)
            }
        })
        //
        if(trs.length === data.length){
            isCanAddProId = false;
        }

        return newData
    }

    // 新增
    $('#addBtn').click(function () {
        if(!isCanAddProId){
            parent.layer.msg(bt.lang('table.externalCorpQuickPay.quickPayInfo.not'));
            return;
        }
        // 如果当前行未被选中，则不能添加;
        var lastTr =$('#qpiBody').find('tr:last-child');
        if(lastTr[0]){
            var proSelectValue = $.trim(lastTr.find('.type-select select').val());
            if(!proSelectValue || !cacheQuickyPayInfo[proSelectValue] || !(cacheQuickyPayInfo[proSelectValue] && cacheQuickyPayInfo[proSelectValue].length)){
                parent.layer.msg(bt.lang('table.externalCorpQuickPay.quickPayInfo.edit'));
                return
            }else {
                lastTr.find('.type-select select').attr('disabled',true)
            }
        }
        // 添加数据
        var html = '<tr>';
        html += '<td class="type-select"><div class="layui-inline"> <div class="bt-biz-selectType" data-url="'+ basePath +'/foreign/fexproduct/getInitProductDate" data-method="get"' +
            '                                 data-valuefield="id" data-textfield="proName" data-name="proId" data-biz-name="proId"' +
            '                                 data-defaultvalue="first" data-callback="proIdCallback" data-beforeInitResiveData="bt.initProductList"></div></div></td>'
        html += '<td class="email-td"></td>'
        html += '<td class="tel-td"></td>'
        html += '<td><a class="edit-btn-1" href="#">'+ bt.lang('common.button.edit') +'</a> '
        html += '<a class="view-btn-1" href="#">'+ bt.lang('common.button.view') +'</a> '
        html += '<a class="del-btn-1" href="#">'+ bt.lang('common.button.delete') +'</a></td>'
        html += '</tr>'

        $('#qpiBody').append(html);
        bt.selectType.init();
    })

    // 编辑
    $('#quicklyPayInfo').on('click','.edit-btn-1',function () {
        openPayInfoWeb($(this).closest('tr'))
        return false
    })
    // 查看
    $('#quicklyPayInfo').on('click','.view-btn-1',function () {
        openPayInfoWeb($(this).closest('tr'),true)
        return false
    })
    // 删除
    $('#quicklyPayInfo').on('click','.del-btn-1',function () {
        var this_ = $(this)
        parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
            // 删除对应缓存的数据
            var type = this_.closest('tr').find('select').val()
            delete  cacheQuickyPayInfo[type];
            this_.closest('tr').remove();
            isCanAddProId = true;
            return false
        })
    })
    // 弹框
    function openPayInfoWeb(currentTr,isView){
        if(!$.trim(currentTr.find('.type-select select').val())){
            parent.layer.msg(bt.lang("table.externalCorpQuickPay.quickPayInfo.select"))
            return
        }
        var url = basePath + "/sys/externalCorp/addOrEditQuickPayInfo?type="+currentTr.find('.type-select select').val();
        // 如果无数据时，提示不能预览
        if(isView && !cacheQuickyPayInfo[currentTr.find('.type-select select').val()]){
            parent.layer.msg(bt.lang("common.msg.temporarily_no_data"), {icon: 2});
            return false
        }
        if(isView){
            url += "&isView=1"
        }
        parent.layer.open({
            type: 2,
            area: ['900px', '700px'],
            maxmin: true,
            content: url
        });
    }

    /**
     * edit
     * *
     */

    if($('#externalCorpId').val()){
        getQuickPayInfo()
    }

    function getQuickPayInfo(){
        $.ajax({
            url: basePath + "/sys/externalCorp/getQuickPayInfo?externalCorpId="+$('#externalCorpId').val(),
            type: "get",
            dataType: "json",
            success: function (data) {
                if(data){
                    renderQuickPay(data);
                    cacheQuickyPayInfo = data;
                }
            }
        });
    }

    function renderQuickPay(data) {
        if(data){
            var html ='';
            for(var key in data){
                // 更新快捷支付信息
                var email = '',tel = '';
                bt.each(data[key],function (i,item) {
                    if(email){
                        email += ','
                    }
                    if(item.email){
                        email += item.email;
                    }
                    if(item.countryCode && item.tel){
                        if(tel){
                            tel += ','
                        }
                        tel += item.countryCode;
                        tel += '-'+item.tel
                    }
                })

                html +='<tr><td class="type-select"><div class="layui-inline">\n' +
                    '                            <div class="bt-biz-selectType" data-url="'+basePath+'/foreign/fexproduct/getInitProductDate" data-method="get"\n' +
                    '                                 data-valuefield="id" data-textfield="proName" data-name="proId" data-biz-name="proId"\n' +
                    '                                 data-defaultvalue="'+key+'" data-callback="proIdCallback" data-disabled="true"></div>\n' +
                    '                        </div></td>'

                html +='<td class="email-td">'+email+'</td>'

                html +='<td class="tel-td">'+tel+'</td>'
                html +='<td>\n' +
                    '                        <a class="edit-btn-1" href="#">'+bt.lang("common.button.edit")+'</a>\n ' +
                    '                        <a class="view-btn-1" href="#">'+bt.lang("common.button.view")+'</a>\n ' +
                    '                        <a class="del-btn-1" href="#">'+bt.lang("common.button.delete")+'</a>\n' +
                    '                    </td>'
                html +='</tr>'
            }
            $('#qpiBody').html(html);
            bt.selectType.init()
        }

    }
});
bt.externalTypeCallback = function(data){
    initSupplier(data.value);
};

function initSupplier(value) {
    if (value == 0){
        selectTypes.get("supplierId").attr("data-required",false);
        $("#supplierId").removeAttr('data-required');
        $(".showSupplierId").hide();
    }else {
        $(".showSupplierId").show();
        selectTypes.get("supplierId").attr("data-required",true);
    }
    form.renderIcon();
}

// 获取快捷支付信息编辑回传的数据
function sendDataFormEdit(data) {
    if(data && data.type){
        // 缓存数据
        cacheQuickyPayInfo[data.type] = data.data;

        // 更新快捷支付信息
        var email = '',tel = '';
        bt.each(data.data,function (i,item) {
            if(email){
                email += ','
            }
            email += item.email;
            if(item.countryCode && item.tel){
                if(tel){
                    tel += ','
                }
                tel += item.countryCode;
                tel += '-'+item.tel
            }
        })

        // type找到对应的行数据
        var selectedSelect = $('#qpiBody').find('select');
        bt.each(selectedSelect,function (i,se) {
            if($(se).val() === data.type){
                $(se).closest('tr').find('.email-td').text(email);
                $(se).closest('tr').find('.tel-td').text(tel);
                return
            }
        })
    }
}

//选择资金收付产品回调
function proIdCallback() {
    // 只要有切换就会清空数据
    cacheQuickyPayInfo = {}
}

//重置页面信息
$("#reset").click(function(){
    location.reload();
    return false ;
});
