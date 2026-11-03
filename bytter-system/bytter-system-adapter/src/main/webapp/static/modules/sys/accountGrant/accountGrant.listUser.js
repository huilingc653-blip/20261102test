bt.use(['datatable', 'jquery','form','corp'], function () {
    var layer = parent ? parent.bt.layer : bt.layer;
    var form = bt.form();
    var $ = bt.jquery;

    var table = bt.datatable.init({
        elem: "#userTable",
        ajax: {
            url: basePath + "/sys/user/datatable.json",
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        scrollY:'500px',
        selectCss: false,
        columns: [
            {data: "loginName"},
            {data: "username", orderable: false},
            {data: "corpName"},
            {
                className: "text-center", orderable: false, render: function (data, type, full, meta) {
                return '<a href="javascript:void(0);" data-row="'+meta.row+'" class="assgin-account">'+bt.lang('accountGrant.assginAccount')+'</a>';
            }}
        ]
    });
    form.on('radio(chooseAssignType)', function(data){
        if(data.value==1){
            location.href = basePath + "/sys/accountGrant/list";
        }else{
            location.href = basePath + "/sys/accountGrant/listUser";
        }
    })

    $(document).on('click','.assgin-account',function(event){
        var data = table.row($(this).data('row')).data();
        var title;
        title = data.corpName + ' - ' + data.username + ' - '+bt.lang('accountGrant.assginAccount');
        layer.open({
            type: 2,
            title:title,
            area: ['1150px', '600px'],
            content: basePath + "/sys/accountGrant/assignAccount/userId/" + data.id
        });
        event.stopPropagation();
    });
    bt.corp.init();
});
