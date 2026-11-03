
bt.use(['form','datatable','corp','select','dicts','dateType','selectType','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    mulselectType=bt.mulselect.init();
    //监听提交
    form.on('submit(queryData)', function(data){
        debugger
        console.log(data)
        // $.post(basePath + "/sys/config/save",data.field,function(result){
        //     parent.layer.alert(result.msg, {icon: result.success?1:2});
        // },"JSON");
        return false;
    });

    form.on('submit(saveData)', function(data){
        console.log(data)
        // $.post(basePath + "/sys/config/save",data.field,function(result){
        //     parent.layer.alert(result.msg, {icon: result.success?1:2});
        // },"JSON");
        return false;
    });

    //生成列表
    // bt.datatable.init({
    //     elem: "#appTable",
    //     colReorder: true,
    //     query: "search",
    //     checkbox:false,
    //     order: [[0,'asc']],
    //     searchForm: {
    //         formElem: "#searchForm",
    //         formFilter: "search"
    //     },
    //     ajax: {
    //         'url': basePath+"/credit/evaluationAnalysis/datatable"
    //     },
    //     buttons: [
    //         {
    //             extend: "colvis",
    //             text: bt.lang('common.button.theadHide'),
    //             columns: ':not(:first):not(:last)'
    //         }
    //     ]
    //     ,columns: [
    //         {data:"dataSourceCategoryName",bSortable:true,className:"center"},
    //         {data:"dataName",bSortable:true,className:"center"},
    //         {data:"createDate",bSortable:true,className:"center"},
    //         {data:null,bSortable:false,className:"center",
    //             render: function (data, type, full, meta) {
    //                 var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
    //                 return html;
    //             }
    //         }
    //     ]
    // });

    //查看
    view = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/credit/creditAnalysis/view/" + data.id
        });
    }
});



