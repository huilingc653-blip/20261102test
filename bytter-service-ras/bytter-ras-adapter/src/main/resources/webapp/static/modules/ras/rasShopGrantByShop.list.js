var corps={};
bt.use(['datatable', 'jquery','form','corp','dicts'], function () {
    $ = bt.jquery,form = bt.form();
    var layer = parent ? parent.bt.layer : bt.layer;
    corps=bt.corp.init();
    bt.dicts.init();
    var table = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        ajax: {
            url: basePath + "/ras/shop/shopGrantShopDatatable",
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        scrollY:'500px',
        selectCss: false,
        columns: [
            {data: "corpName"},
            {data: "name",
                render: function ( data, type, full, meta ) {
                return '('+full.code+')'+full.name;
            }},
            {data: "shopTypeName"},
            {data: "shopName"},
            {
                className: "text-center", orderable: false, render: function (data, type, full, meta) {
                return '<a href="javascript:void(0);" data-row="'+meta.row+'" class="assgin-account">'+bt.lang('rasShopGrant.assginUser')+'</a>';
            }}
        ]
    });
    form.on('radio(chooseAssignType)', function(data){
        if(data.value==1){
            location.href = basePath + "/ras/shopGrant/list";
        }else{
            location.href = basePath + "/ras/shopGrant/listShop";
        }
    })

    $(document).on('click','.assgin-account',function(event){
        var data = table.row($(this).data('row')).data();
        var title;
        title = data.corpName + ' - ' + data.name + ' - '+bt.lang('rasShopGrant.assginUser');
        layer.open({
            type: 2,
            title:title,
            area: ['1150px', '600px'],
            content: basePath + "/ras/shopGrant/assginUser/shopId/" + data.id
        });
        event.stopPropagation();
    });
});
