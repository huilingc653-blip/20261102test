bt.use(['datatable', 'jquery','form','corp','dicts'], function () {
    var layer = parent ? parent.bt.layer : bt.layer;
    var $ = bt.jquery;
    var form = bt.form();
    var userId = $("#userId").val();
    bt.corp.init();
    bt.dicts.init();
    var assigned;
    var table;
    var tableBind = true;
    bt.getAssignData = function () {
        $.ajax({
            type: "get",
            url: basePath + "/ras/shopGrant/assigned",
            dataType:"json",
            async: false,
            data:{shopId:null,userId:userId},
            success: function(result){
                assigned = result;
            }
        });
    };
    bt.initTable = function () {
        if (table) {
            table.destroy();
        }
        bt.getAssignData();
        //生成列表
        table = bt.datatable.init({
            elem: "#appTable",
            ajax: {
                url: basePath + "/ras/shop/shopGrantShopDatatable",
            },
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            scrollY:'400px',
            selectCss: false,
            colReorder:false,
            columns: [
                {data: "corpName"},
                {data: "name",
                    render: function ( data, type, full, meta ) {
                        return '('+full.code+')'+full.name;
                    }},
                {data: "shopTypeName"},
                {data: "shopName"},
                {
                    className: "text-center",orderable: false, render: function (data, type, full, meta) {
                    return '<input type="checkbox" name="like[write]" lay-filter="query" lay-skin="primary" ' + (checkHasUserIdAndPermission(full,1)?'checked="checked"':'') + ' />';
                }},
                {
                    className: "text-center", orderable: false,render: function (data, type, full, meta) {
                    return '<input type="checkbox" name="like[write]" lay-filter="canAudit" lay-skin="primary" ' + (checkHasUserIdAndPermission(full,2)?'checked="checked"':'') + ' />';
                }}
            ],
            btDrawCallback: function( settings ) {
                checkCurrentPageIsAllChecked();
            }
        });

        //发生异步请求表格数据的时候，清楚dom中的data
        table.on('preXhr.dt', function ( e, settings, data ) {
            query.removeData();
            canAudit.removeData();
            bt.getAssignData();
        });
        if (tableBind) {
            tableBind = false;
        };

    };
    bt.initTable();

    $("#submitAssign").click(function(){
        if(!table){
            layer.alert(bt.lang('accountGrant.sysAccountType'), {icon: 2});
            return;
        }
        var shopGrant = [];
        var queryData = query.data();
        var canAuditData = canAudit.data();

        table.rows().nodes().data().each(function (k, v) {
            var kd = k.id;
            if (!shopGrant[kd]) {
                shopGrant[kd] = {};
            }
            shopGrant[kd].shopId = kd;
            shopGrant[kd].userId = userId;

        });

        $.each(queryData,function(kd,e){
            if(shopGrant[kd]){
                shopGrant[kd]['canQuery'] = 1;
            }
        });
        $.each(canAuditData,function(kd,e){
            if(shopGrant[kd]){
                shopGrant[kd]['canAudit'] = 1;
            }
        });

        var postArr = [];
        for (var k in shopGrant) {
            postArr.push(shopGrant[k]);
        }

        $.ajax({
            type: "POST",
            url: basePath + "/ras/shopGrant/assignShop",
            data: JSON.stringify(postArr),//将对象序列化成JSON字符串
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    //清空存储在dom的数据
                    query && query.removeData();
                    canAudit && canAudit.removeData();
                    //重新加载授权数据
                    bt.getAssignData();
                }
            }
        });
    });

    /**
     * 检查当前页面是否全选了权限
     * 如果全选了，则将最上面的复选框选上。
     */
    function checkCurrentPageIsAllChecked() {
        var dataList = [];
        table.rows().nodes().data().each(function (data, index) {
            dataList.push(data);
        });
        checkAccountListIsAllChecked(dataList);
    }

    /**
     * 检查数据列表是否全选了权限
     * @param dataList
     */
    function checkAccountListIsAllChecked(dataList) {
        if (dataList) {
            var queryAllChecked = true;
            var canAuditAllChecked = true;

            for (var i = 0; i < dataList.length; i++) {
                if (!checkHasUserIdAndPermission(dataList[i], 1)) {
                    queryAllChecked = false;
                }
                if (!checkHasUserIdAndPermission(dataList[i], 2)) {
                    canAuditAllChecked = false;
                }
            }

            var queryAllElement = $(".queryAll");
            var canAuditAllElement = $(".canAuditAll");
            if (queryAllChecked) {
                queryAllElement && queryAllElement.prop("checked", true);
            } else {
                queryAllElement && queryAllElement.prop("checked", false);
            }
            if (canAuditAllChecked) {
                canAuditAllElement && canAuditAllElement.prop("checked", true);
            } else {
                canAuditAllElement && canAuditAllElement.prop("checked", false);
            }
            form.render('checkbox', 'queryAll');
            form.render('checkbox', 'canAuditAll');
        }
    }
    /**
     * 设置是否选中成功
     * @param userId
     * @param type
     * @returns {boolean}
     */
    function checkHasUserIdAndPermission(data,type){
        for(var k in assigned){
            var shopGrant = assigned[k];
            if(shopGrant.shopId != data.id){
                continue;
            }
            switch (type){
                case 1:
                    if(shopGrant.canQuery==1){
                        query.data(data.id,data);
                        return true
                    }
                    break;
                case 2:
                    if(shopGrant.canAudit==1){
                        canAudit.data(data.id,data);
                        return true
                    }
                    break;
            }
        }
        return false;
    }
    function setOrRemove(index,jqueryDom,checked){
        var tableData = table.row(index).data();
        if(checked){
            jqueryDom.data(tableData.id,tableData);
        }else{
            jqueryDom.removeData(tableData.id);
        }
    }
    function checkAll(data,dataDom){
        var index = data.othis.parents('th').index();
        table.rows().nodes().to$().each(function(){
            var that = $(this);
            var child;
            if(data.elem.checked){
                child = that.find('td:eq('+index+') input[type="checkbox"]');
            }else{
                child = that.find('td:eq('+index+') input[type="checkbox"]');
            }
            child.each(function(index, item){
                item.checked = data.elem.checked;
            });
            setOrRemove(that.index(),dataDom,data.elem.checked);

        });
        form.render('checkbox');
    }
    var query = $('.query');
    var canAudit = $('.canAudit');

    form.on('checkbox(query)', function(data){
        setOrRemove(data.othis.parents('tr').index(),query,data.elem.checked);
        if(!data.elem.checked){
            query.find('input[type="checkbox"]').each(function(index, item){
                item.checked = data.elem.checked;
            });
        }
        form.render('checkbox');
    })
    form.on('checkbox(queryAll)', function(data){
        checkAll(data,query);
    })

    form.on('checkbox(canAudit)', function(data){
        setOrRemove(data.othis.parents('tr').index(),canAudit,data.elem.checked);
    });
    form.on('checkbox(canAuditAll)', function(data){
        checkAll(data,canAudit);
    })

});
