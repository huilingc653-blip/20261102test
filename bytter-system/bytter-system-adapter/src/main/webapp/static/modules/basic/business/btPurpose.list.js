bt.use(['form','datatable','corp','dicts', 'btImport'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dicts.init();

    importExcel = function () {
        up = bt.btImport.init({
            code: "40000003",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/basic/business/btpurpose/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            }
        });
    }
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/basic/business/btpurpose/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            }, {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
               {data:"purpose",bSortable:true},
               {data:"sysBusType",bSortable:true,
                   render: function ( data, type, full, meta ) {
                        if(!full.sysBusType){
                            return "";
                        }
                	   // 分割用途所属类别组装输出
                	   var sysBusTypes = full.sysBusType.split(",");
                	   var sysBusType;
                	   $.each(sysBusTypes, function(i, busType){
                		   if(null == sysBusType ){
                			   sysBusType = bt.dict("btPurpose.sysBusType", busType);
		                   	}else{
		                   		sysBusType += "," + bt.dict("btPurpose.sysBusType", busType);
		                   	}
                	   });
                       return sysBusType;
                   }
               },
               {data:"corpName",bSortable:false,render:function (data, type, full, meta) {
                   return '<div title="'+full.corpName+'" class="ellipsis" style="max-width: 500px;">'+full.corpName+'</div>'
                   // if(!full.corpName){
                   //     return "";
                   // }
                   // var corps = full.corpName.split(",");
                   // if (corps.length > 2){
                   //     return corps[0]+","+corps[1]+" ...";
                   // }else{
                   //     return full.corpName;
                   // }

               }},
                {data:"curName",bSortable:false,render:function (data, type, full, meta) {
                        if(!full.curName){
                            return "";
                        }else{
                            return '<div title="'+full.curName+'" class="ellipsis" style="max-width: 500px;">'+full.curName+'</div>'
                        }
                    // var curs = full.curName.split(",");
                    // if (curs.length > 4){
                    //     return curs[0]+","+curs[1]+","+curs[2]+","+curs[3]+" ...";
                    // }else{
                    //     return full.curName;
                    // }

                }},
               {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                   var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode">|</span>\
                   <a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>\
                   <a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                   return html;
               }}
           ],
        colResize:{
            resizeCallback:function () {
                console.log(arguments);
                $("#appTable_wrapper .ellipsis").css('max-width','none');
            }
        }

    });
});

function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('table.btPurpose.btn.create'),basePath + "/basic/business/btpurpose/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/btpurpose/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/basic/business/btpurpose/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/btpurpose/view/" + data.id
    });
}

function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('table.btPurpose.btn.update'),basePath + "/basic/business/btpurpose/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/basic/business/btpurpose/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/basic/business/btpurpose/delete/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
