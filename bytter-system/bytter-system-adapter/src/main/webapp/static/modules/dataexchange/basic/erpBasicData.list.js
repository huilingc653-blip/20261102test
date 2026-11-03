bt.use(['form','datatable','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    var selType = bt.selectType.init();
    //生成列表

    var datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',

        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            callback: function (data) {
                $("#hidTypeCode").val(selType.get("typeCode").val());
                $("#hidDataTypeCode").val(selType.get("dataTypeCode").val());
            }
        },
        ajax: {
            'url': basePath+"/dataexchange2/basic/erpbasicdata/datatable"
        },
        buttons: [

        ]
        ,columns: [
            {data:"bytterCode",bSortable:true,render:function (data, type, full, meta) {
                return full.bytterCode+'<input id="' + meta.row + 'bytterCode"  type="hidden" value="'+full.bytterCode+'">'
                +'<input id="' + meta.row + 'id"  type="hidden" value="'+(full.id == null ? '':full.id)+'">';
            }},
            {data:"bytterName",bSortable:false},
            {data:"erpCode",bSortable:true,render:function (data, type, full, meta) {
                return '<input type="text"  id="' + meta.row + 'erpCode" value="'+(full.erpCode == null ? '':full.erpCode)+'" class="layui-input" lay-verify="strlength" data-maxlength="50" autocomplete="off"/>';
            }},
            {data:"erpName",bSortable:true,render:function (data, type, full, meta) {
                return '<input type="text"  id="' + meta.row + 'erpName" value="'+(full.erpName == null ? '':full.erpName)+'" class="layui-input" lay-verify="strlength" data-maxlength="100" autocomplete="off"/>';
            }},
            {data:"erpReverse",bSortable:true,render:function (data, type, full, meta) {
                return '<input type="text"  id="' + meta.row + 'erpReverse" value="'+(full.erpReverse == null ? '':full.erpReverse)+'" class="layui-input" lay-verify="strlength" data-maxlength="100" autocomplete="off"/>';
            }}
            // ,
            // {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            //     var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>\
            //     <a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>\
            //     <a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
            //     return html;
            // }}
        ]
        });

    //监听提交
    form.on('submit(submit)', function (data) {
        if(!form.verifyFun('#appTable')){
            return false;
        }
        var accState = $(this).attr("value");
        var dt = datatable.rows('.selected').data();
        var len = dt.length;
        if(len<1){
            layer.msg(bt.lang('table.fcBankBill.msg.chooseLeastOne'));
            return false;
        }
        var indexs=datatable.rows('.selected').indexes();
        var rowData = dt.splice(0,len);
        var obj = new Object();
        obj.dataTypeCode=$("#hidDataTypeCode").val();
        obj.typeCode=$("#hidTypeCode").val();
        for(var i = 0 ; i < rowData.length ; i++){
            obj["erpBasicDataVOS["+ i +"].bytterCode"] = $("#"+indexs[i]+"bytterCode").val();
            obj["erpBasicDataVOS["+ i +"].erpCode"] = $("#"+indexs[i]+"erpCode").val();
            obj["erpBasicDataVOS["+ i +"].erpName"] = $("#"+indexs[i]+"erpName").val();
            obj["erpBasicDataVOS["+ i +"].erpReverse"] = $("#"+indexs[i]+"erpReverse").val();
            obj["erpBasicDataVOS["+ i +"].id"] = $("#"+indexs[i]+"id").val();
        };
        // console.log(obj);
        var action = "/dataexchange2/basic/erpbasicdata/saveOrUpdate";
        $.post(basePath + action, obj, function (result) {
            if (result.success) {
                datatable.draw();
            }
            parent.layer ? parent.layer.alert(result.msg, {
                icon: result.success ? 1 : 2
            }) : layer.alert(result.msg, {
                icon: result.success ? 1 : 2
            });
        }, 'JSON');
        return false;
    });
});
