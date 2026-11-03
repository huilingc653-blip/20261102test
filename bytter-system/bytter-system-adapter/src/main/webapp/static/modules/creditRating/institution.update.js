var dicts,dateTypes,warnDataTable,moneyType,bankAccAtt,bankAccNature,bttable,bookBttable,visitBtTable,region,form;
bt.use(['form','element','datatable','select','checkboxs', 'btdate','selectType','moneyType','corp','dicts','dateType'
    ,'mulCorp','mulselect','mulBankAccInfo','mulAccInfo','domesticAndOverseasBank','bttable','btUpload','region','btImport'], function() {
    var $ = bt.jquery;
        form = bt.form();
    // 1. 国家/地区
    var regionalCodeName = $('#regionalCodeName').val();
    // 中国
    if(regionalCodeName === '中国'){
        $("#addrDiv").show();
        $("#addressDiv").hide();
    }
    selectType = bt.selectType.init();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    dateTypes = bt.dateType.init();
    region = bt.region.init();

    //一些事件触发
    var oldOrgKey ='';
    element.on('tab(institution)', function (data) {
        if ("evaluation" == $(data.elem.context).attr('lay-id')) {
            var currentOrgKey = $("#org-box").find('> div:not(.layui-hide)').find('input').val();

            if(oldOrgKey != currentOrgKey){
                //
                updateOrgEvaluateInfo(currentOrgKey);

                oldOrgKey = currentOrgKey;
            }
        }
    });


    var loadingHmtl = '<tr><td colspan="5"></td></tr>'
    function updateOrgEvaluateInfo(orgKey){
        $("#infoTbody").html(loadingHmtl);
        $.post(basePath + "/credit/crTaskOrg/queryByOrgName",{"orgName":orgKey},function (result) {
            // infoTbody
            // parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
            if (result.data.length > 0) {
                updateInfoTbody(result.data)
            }else {
               var html = '<tr ><td class="center" style="color:red" colspan="5">'+bt.lang('cms.crTaskOrg.notask')+'</td></tr>'
                $("#infoTbody").html(html);
            }
        }, 'JSON');
    }

    function updateInfoTbody(list){
        var html = '';
        if(list && list.length){
            $.each(list,function (i,item){
                html += '<tr>'
                html += '<td>'+(item.orgAvg||'')+'</td>'
                html += '<td>'+(item.evaluationLevel||'')+'</td>'
                html += '<td>'+(bt.date.format(item.evaluateStartDate, "yyyy-MM-dd")||'')+'</td>'
                html += '<td>'+(bt.date.format(item.evaluateEndDate, "yyyy-MM-dd")||'')+'</td>'
                html += '<td><a class="review-detail" href="#" data-id="'+item.id+'" data-orgid="'+item.orgId+'">明细</a></td>'
                html += '</tr>'
            })
        }else{
            html = '<tr><td colspan="5">暂时无数据</td></tr>'
        }
        $("#infoTbody").html(html);
    }

    // 明细
    $("#infoTbody").on('click','.review-detail',function (data){
        //data.taskNo
        // debugger;
        // var taskNo = $(this).data('task-no');
        reviewDetailFunction($(this).data());

    })

    form.verify({
        emailFex: function (value) {
            var vals = value.split(";");
            for (var i = 0; i < vals.length; i++) {
                if (vals[i] && !/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(vals[i])) {
                    return bt.lang('sys.form.verify.emailError')//'邮箱格式不正确';
                }
            }
        }
    });


    var domesticAndOverseasBank = bt.domesticAndOverseasBank.init();// 境内境外联行号
    bt.domesticAndOverseasBankBeforeClick = function () {
        domesticAndOverseasBank.set({
            "joinBankType": true,
        });
        return true;
    }

    var uploader = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    /**
     *  生成股权结构列表
     */
    bttable = bt.bttable.init({
        elem: "#ownershipStructure",
        mulselect:false,
        checkbox: false,
        'url': basePath + "/credit/ownershipStructure/detailDatatable?ids="+$("#ssIds").val(),
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    ownershipStructureAdd();
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcelToOwnershipStructure();
                }
            },
            {
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {
                    exportExcelToOwnershipStructure();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {data:"rowNum",className:"center",render:function(data, row){
                    return data.rowNum;
                }},
            {data:"investor",className:"center",render:function(data, row){
                    return data.investor;
                }},
            {data:"financingWay",className:"center",render:function(data, row){
                    // return data.financingWay;
                    return bt.dict("cms.equity.financingWay", data.financingWay);
                }},
            {data:"investmentMethod",className:"center",render:function(data, row){
                    // return bt.dict("cms.equity.invWay", data.invWay);
                    // return data.investmentMethod;
                    return bt.dict("cms.equity.invWay", data.investmentMethod);
                }},
            {data:"contributionAmount",className:"textRight",render: function ( data ){
                    return bt.money.format(data.contributionAmount);
                }},
            {data:"shareholdingRatio",className:"center",render:function(data, row){
                    return data.shareholdingRatioStr != null ? data.shareholdingRatioStr+"%" : " ";
                }},
            {data:"assetName",className:"center",render:function(data, row){
                    return data.assetName;
                }},
            {data:"rmk",className:"center",render:function(data, row){
                    return data.rmk;
                }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    html += '<a class="" href="#" id="'+data.id+'" onclick="updateOwnershipStructure(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="'+data.id+'" onclick="ownershipStructureDel(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="'+data.id+'" onclick="ownershipStructureView(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }}
        ]
    });

    /**
     *  生成股权结构列表
     */
    bookBttable = bt.bttable.init({
        elem: "#contactAddressBook",
        mulselect:false,
        checkbox: false,
        'url': basePath + "/credit/contactAddressBook/detailDatatable?ids="+$("#bookIds").val(),
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    addBook();
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcelToContactAddressBook();
                }
            },
            {
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {
                   exportExcelToContactAddressBook();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {data:"rowNum",className:"center",render:function(data, row){
                    return data.rowNum;
                }},
            {data:"investor",className:"center",render:function(data, row){
                    return data.name;
                }},
            {data:"financingWay",className:"center",render:function(data, row){
                    return data.sex;
                }},
            {data:"investmentMethod",className:"center",render:function(data, row){
                    return data.job;
                }},
            {data:"contributionAmount",className:"textRight",render: function ( data ){
                    return data.telephoneNum;
                }},
            {data:"shareholdingRatio",className:"center",render:function(data, row){
                    return data.email
                }},
            {data:"assetName",className:"center",render:function(data, row){
                    return data.weChatNum;
                }},
            {data:"qqNum",className:"center",render:function(data, row){
                    return data.qqNum;
                }},
            {data:"rmk",className:"center",render:function(data, row){
                    return data.rmk;
                }},
            {data:"creator",className:"center",render:function(data, row){
                    return data.creator;
                }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    html += '<a class="" href="#" id="'+data.id+'" onclick="updateBook(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="'+data.id+'" onclick="delBook(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="'+data.id+'" onclick="viewBook(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }}
        ]
    });

    /**
     *  生成 拜访记录登记 列表
     */
    visitBtTable = bt.bttable.init({
        elem: "#visitRecordRegistration",
        mulselect:false,
        checkbox: false,
        'url': basePath + "/credit/visitRecordRegistration/detailDatatable?ids="+$("#visitIds").val(),
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    visitAdd();
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcelToVisitRecordRegistration();
                }
            },
            {
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {
                    exportExcelToVisitRecordRegistration();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {data:"rowNum",className:"center",render:function(data, row){
                    return data.rowNum;
                }},
            {data:"visitingMatters",className:"center",render:function(data, row){
                    return data.visitingMatters;
                }},
            {data:"visitingPersonnel",className:"center",render:function(data, row){
                    return data.visitingPersonnel;
                }},
            {data:"visitingObj",className:"center",render:function(data, row){
                    // return bt.dict("cms.equity.invWay", data.invWay);
                    return data.visitingObj;
                }},
            {data:"visitingObjJob",className:"center",render:function(data, row){
                    return data.visitingObjJob;
                }},
            {data:"visitDate",className:"center",render: function ( data ){
                    return  data.visitDateStr;
                }},
            {data:"rmk",className:"center",render:function(data, row){
                    return data.rmk;
                }},
            {data:"creator",className:"center",render:function(data, row){
                    return data.creator;
                }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    html += '<a class="" href="#" id="'+data.id+'" onclick="visitUpdate(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="'+data.id+'" onclick="visitDel(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="'+data.id+'" onclick="visitView(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }}
        ]
    });
    //监听提交
    form.on('submit', function(data){
        bt.loading.show();
        var opType=$(data.elem).val();

        //机构名称按机构类别取对应控件的值：总行取银行类型下拉，支行取联行号组件选中的银行名称（后端按名称反查），其它类别取手工录入值。
        //三个控件都在表单里，未选中的类别只是被隐藏、值照样提交，所以不能再按"institutionName 为空才覆盖"来判断，
        //否则隐藏控件的残留值会把当前类别真正填写的名称顶掉，导致暂存/提交后机构名称没修改成功。
        var institutionName = '';
        if ("001" == data.field.institutionType) {
            institutionName = data.field.institutionName;
        } else if ("002" == data.field.institutionType) {
            institutionName = data.field.shouBankName;
        } else {
            institutionName = data.field.otherInstitutionName;
        }
        if (institutionName != undefined && institutionName != null && $.trim(institutionName) != "" && institutionName != " ") {
            data.field.institutionName = $.trim(institutionName);
        } else {
            //没填就不提交该字段，后端 copyNotNullProperties 不覆盖null，库里原值保留
            delete data.field.institutionName;
        }

        //获取附件id
        data.field.attachmentId=uploader.getGroupId();
        var nm = $(this).attr("id");
        // console.log("nm =", nm);
        // console.log("data.field =", data.field);

        var action = "/credit/rawData/modifyInstitution/"+nm;
        // var action = "/credit/rawData/register/submit";
        $.post(basePath + action, data.field, function(result){
            parent.layer.msg(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    location.reload();
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                    if(data.field.id == ""){
                        bt.navtab.refreshCurrent();
                    }else{
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshCurrent();
                    }
                }
            }else{
                bt.loading.hide();
            }
        },'JSON');
        return false;
    });

});


//添加 股权结构 信息
function ownershipStructureAdd() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/credit/ownershipStructure/add/" + $("#id").val());
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/ownershipStructure/add/" + $("#id").val(),
        end: function () {
            location.reload();
        }
    });
}
//修改 股权结构 信息
function ownershipStructureView(el){
    var id=$(el).attr("id")
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/credit/ownershipStructure/viewOwnershipStructure/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/ownershipStructure/viewOwnershipStructure/" + id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

//修改 股权结构 信息
function updateOwnershipStructure(el){
    var id=$(el).attr("id")
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/credit/ownershipStructure/update/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/ownershipStructure/update/" + id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

//刪除 股权结构 信息
function ownershipStructureDel(el){
        var id=$(el).attr("id")
        parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
            bt.loading.show();
            $.post(basePath + "/credit/ownershipStructure/deleteOwnershipStructure/" + id, function(result){
                bt.loading.hide();
                var ssIds=$("#ssIds").val()
                var idArray=ssIds.split(',')
                for(var i=0;i<idArray.length;i++){
                    if(idArray[i]==id){
                        idArray.splice(i,1)
                    }
                }
                ssIds=idArray.toString()
                $("#ssIds").val(ssIds)
                bttable.reload();
                parent.layer.msg(result.msg, {icon: result.success?1:2});
            },'JSON');
        });
}

importExcelToOwnershipStructure = function () {
    up = bt.btImport.init({
        code: "120000001",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                // url: basePath + "/cms/equity/info/saveImport", credit/ownershipStructure
                url: basePath + "/credit/ownershipStructure/saveImport",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(up.getSelectData()),
                success: function (result) {
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    if (result.success) {
                        var ssIds=$("#ssIds").val()
                        if (ssIds == "") {
                            ssIds = result.obj
                        } else {
                            ssIds = ssIds + "," + result.obj
                        }
                        bttable.reload({url:basePath + "/credit/ownershipStructure/detailDatatable?ids="+ssIds});
                        bt.jquery("#ssIds").val(ssIds);
                        console.log(ssIds)
                    }
                }
            });
        }
    });
};

exportExcelToOwnershipStructure = function () {
    var url = basePath + "/credit/ownershipStructure/exportAll?id=" + $("#ssIds").val();
    window.location.href = url;

}

//添加 联想人通讯录 信息
function addBook() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/credit/contactAddressBook/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/contactAddressBook/add",
        end: function () {
            location.reload();
        }
    });
}

//查看 联想人通讯录 信息
function viewBook(el){
    var id=$(el).attr("id")
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/credit/contactAddressBook/viewContactAddressBook/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/contactAddressBook/viewContactAddressBook/" + id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

//修改 联想人通讯录 信息
function updateBook(el){
    var id=$(el).attr("id")
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/credit/contactAddressBook/update/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/contactAddressBook/update/" + id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

//刪除 联想人通讯录 信息
function delBook(el){
    var id=$(el).attr("id")
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        bt.loading.show();
        $.post(basePath + "/credit/contactAddressBook/deleteContactAddressBook/" + id, function(result){
            bt.loading.hide();
            var bookIds=$("#bookIds").val()
            var idArray=bookIds.split(',')
            for(var i=0;i<idArray.length;i++){
                if(idArray[i]==id){
                    idArray.splice(i,1)
                }
            }
            bookIds=idArray.toString()
            $("#bookIds").val(bookIds)
            bookBttable.reload();
            parent.layer.msg(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}


//联系人通讯录 导入
importExcelToContactAddressBook = function () {
    up = bt.btImport.init({
        code: "120000002",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                // url: basePath + "/cms/equity/info/saveImport",
                url: basePath + "/credit/contactAddressBook/saveImport",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(up.getSelectData()),
                success: function (result) {
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    if (result.success) {
                        var bookIds=$("#bookIds").val()
                        if (bookIds == "") {
                            bookIds = result.obj
                        } else {
                            bookIds = bookIds + "," + result.obj
                        }
                        bookBttable.reload({url:basePath + "/credit/contactAddressBook/detailDatatable?ids="+bookIds});
                        bt.jquery("#bookIds").val(bookIds);
                        // console.log(bookIds)
                    }
                }
            });
        }
    });
};
//联系人通讯录 导出
exportExcelToContactAddressBook = function () {
    var url = basePath + "/credit/contactAddressBook/exportAll?id=" + $("#bookIds").val();
    window.location.href = url;

}

//添加 拜访记录登记 信息
function visitAdd() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/credit/visitRecordRegistration/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/visitRecordRegistration/add",
        end: function () {
            location.reload();
        }
    });
}

//查看 拜访记录登记 信息
function visitView(el){
    var id=$(el).attr("id")
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/credit/visitRecordRegistration/viewVisitRecordRegistration/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/visitRecordRegistration/viewVisitRecordRegistration/" + id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

//修改 拜访记录登记 信息
function visitUpdate(el){
    var id=$(el).attr("id")
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/credit/visitRecordRegistration/update/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/visitRecordRegistration/update/" + id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

//刪除 拜访记录登记 信息
function visitDel(el){
    var id=$(el).attr("id")
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        bt.loading.show();
        $.post(basePath + "/credit/visitRecordRegistration/deleteVisitRecordRegistration/" + id, function(result){
            bt.loading.hide();
            var visitIds=$("#visitIds").val()
            var idArray=visitIds.split(',')
            for(var i=0;i<idArray.length;i++){
                if(idArray[i]==id){
                    idArray.splice(i,1)
                }
            }
            visitIds=idArray.toString()
            $("#visitIds").val(visitIds)
            visitBtTable.reload();
            parent.layer.msg(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}


//拜访记录登记 导入
importExcelToVisitRecordRegistration = function () {
    up = bt.btImport.init({
        code: "120000003",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                // url: basePath + "/cms/equity/info/saveImport",
                url: basePath + "/credit/visitRecordRegistration/saveImport",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(up.getSelectData()),
                success: function (result) {
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    if (result.success) {
                        var visitIds=$("#visitIds").val()
                        if (visitIds == "") {
                            visitIds = result.obj
                        } else {
                            visitIds = visitIds + "," + result.obj
                        }
                        visitBtTable.reload({url:basePath + "/credit/visitRecordRegistration/detailDatatable?ids="+visitIds});
                        bt.jquery("#visitIds").val(visitIds);
                        // console.log(ssIds)
                    }
                }
            });
        }
    });
};

//联系人通讯录 导出
exportExcelToVisitRecordRegistration = function () {
    var url = basePath + "/credit/visitRecordRegistration/exportAll?id=" + $("#visitIds").val();
    window.location.href = url;

}

function instTypesCallBack(data) {
    // org-box
    $("#org-box").find('>div').addClass('layui-hide');
    $("#institutionName2").removeAttr("lay-verify");
    // 001 headOffice 002 branchOffice  other
    if(data.value == "001"){
        $("#headOffice").removeClass('layui-hide');
        $("#bankTypeTR").show()
    }else if(data.value == "002"){
        $("#branchOffice").removeClass('layui-hide');
        selectType.get("institutionName").attr({"data-required":false});
        $("#bankTypeTR").show()
    }else{
        $("#other").removeClass('layui-hide');
        selectType.get("institutionName").attr({"data-required":false});
        $("#institutionName2").attr("lay-verify","required");
        $("#bankTypeTR").hide();
    }
}

function initBankTypeCallBack(data) {
    $("#bankTypeId").val(data.id);
    if(data.bankkind) {
        dicts.get("bankType").val(data.bankkind);
    }else {
        dicts.get("bankType").val('');
    }
}


function resetPlaceSelectType(attrObj){
    var $elem = $('[data-name="place"]');
    for(var key in attrObj){
        $elem.data(key,attrObj[key]);
    }
    $elem.html('').removeClass('init');
}

/**
 * 设置国家
 * @param data
 * @param elem
 * @param bv
 */
function setArea(data,elem,bv){
    var url = basePath + "/basic/business/syscountrycust/listSysCountryArea/" + data.id;
    resetPlaceSelectType(
        {"value": "", "url": url, "required":false
        })
    if (data.namezh == "中国") {
        region.get('payeeAddr').set([{
            name: 'prov',
            defaultValue: '',
            required:true
        }, {
            name: 'city',
            defaultValue: '',
            required:true
        }]);
        $('#addressDiv').hide();
        $('#addrDiv').show();
    } else {

        region.get('payeeAddr').set([{
            name: 'prov',
            defaultValue: '',
            required:false
        }, {
            name: 'city',
            defaultValue: '',
            required:false
        }]);
        $('#addressDiv').show();
        $('#addrDiv').hide();
    }
    selectType.render();

    form.renderIcon();
}

//境内境外银行选择回调
function domesticAndOverseasBankCallback(data, dom) {
    // debugger
    $("#shouBankName").val(data.bankName);
    $("#bankCode").val(data.bankCode);
    // $("#shouBankNameId").val(data.bankCode);
    $.ajax({
        url: basePath + "/basic/business/btbanktype/getBankType",
        method: 'POST',
        data: {bankTypeId: data.bankTypeId},
        dataType: 'json',
        success: function (data) {
            if (data) {
                if(data.bankKind){
                    dicts.get("bankType").val(data.bankKind);
                }else {
                    dicts.get("bankType").val("");
                }
            }else {
                dicts.get("bankType").val("");
            }
        }
    })
}

//关闭页面
function closeForm(){
    //TODO 关闭页面
}

//明细
function reviewDetailFunction(obj) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/credit/crTaskOrg/reviewTaskDetails/"+obj.id +'/'+obj.orgid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/crTaskOrg/reviewTaskDetails/"+obj.id +'/'+obj.orgid,
        end: function () {
            location.reload();
        }
    });
}
