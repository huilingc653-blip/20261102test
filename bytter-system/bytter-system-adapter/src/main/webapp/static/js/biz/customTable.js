/**
 * 业务组件：自定义表格
 * 刘勇军
 * 2018年12月10日
 */
bt.define(['jquery','form','datatablecore','fixedHeader','money','fixedColumns'],function(exports){
    var $ = bt.jquery,form = bt.form();

    var Ctable = function(){
        this.initConfig = {
            elem:'',
            serverSide:false,
            ajax:'',
            dom:'rt<"jump_page">lip',
            // destroy: true,
            searching: false,
            ordering:false,
            bAutoWidth:true,
            data:[],//表格总数据
            allData:[],//表格所有数据
            mergeColumn:[],//需要合并的列索引
            destory:true,
            paging:true,
            checkbox:true,//默认为复选框
            mulselect:true,//默认为多选
            colReorder:true,//固定列
            //fixedHeader:true,
            resize:true,
            scrollX:true,
            // fixedColumns: {
            //     leftColumns:1
            // },
            fixedHeader: {
                header: true,
                //footer: true
            },
            fixedColumns:false,
            lengthMenu:[10, 25, 50,100,500],//每页显示多少条
            scrollY:'300px',//设置表格固定高度 360
        };
        this.isInit = true;
        this.selectedStyle= "selected";
        this.pageClassName = 'bt-customTablePage'
    }
    var lang = {
        sProcessing: "<div class='loader'>"+bt.lang('sys.notification.loading')+"</div>",//加载中...
        // sProcessing: "<div class='loader'>bt.lang('common.table.loading')</div>",//加载中...
        //sProcessing: "<div class='loader'><i class='layui-icon layui-anim layui-anim-rotate layui-anim-loop' style='font-size: 40px'></i></div>",//
        sLengthMenu:bt.lang('common.table.lengthMenu') ,//"每页 _MENU_ 项",
        // sZeroRecords: "换个搜索词试试呢？暂无数据",
        sZeroRecords: "暂无数据",
        sInfo:bt.lang('common.table.info') ,//"当前显示第 _START_ 至 _END_ 项，全部 _TOTAL_ 项。",
        sInfoEmpty:bt.lang('common.table.infoEmpty') ,//"当前显示第 0 至 0 项，全部 0 项",
        sInfoFiltered:bt.lang('common.table.infoFiltered') ,//"(由 _MAX_ 项结果过滤)",
        sInfoPostFix: "",
        sSearch: "本地搜索：",
        sUrl: "",
        sEmptyTable:bt.lang('common.msg.temporarily_no_data') ,//"暂无数据",
        sLoadingRecords:bt.lang('common.table.loading'),//"载入中...",
        sInfoThousands: ",",
        oPaginate: {
            sFirst:bt.lang('common.table.firstPage'),//"首页",
            //sPrevious:bt.lang('common.table.previousPage') ,//"上一页",
            sPrevious:'< ' ,//"上一页",
            //sNext:bt.lang('common.table.nextPage') ,//"下一页",
            sNext:'> ' ,//"下一页",
            sLast: bt.lang('common.table.lastPage'),//"最后页",
            sJump:bt.lang('common.table.jump'),//"跳转至"
        },
        oAria: {
            sSortAscending:bt.lang('common.table.sortAscending'),//":以升序排列此列",
            sSortDescending:bt.lang('common.table.sortDescending') //": 以降序排列此列"
        }
    };

    Ctable.fn = Ctable.prototype;

    //合并参数
    Ctable.fn.set = function(options){
        this.initConfig.language = lang;


        if(!this.isInit){
            $(this.config.elem).DataTable().destroy();//被
            $(this.config.elem).find('tbody').remove();
        }


      //  $.extend(true,this.initconfig,options);//合并参数
        this.config = $.extend(true,{},this.initConfig,options)

        this.config.allData = options.allData;

        if(options.summary){ //当有汇总行时，表格默认高度为360px
            this.config.scrollY = '360px'
        }
    }

    //渲染表格
    Ctable.fn.render = function(){

        //设置头部全选框
        this.tableHeaderCallback();

        // 设置设置columns信息 针对复选框的
        this.setcolumns();

        //设置表格数据
        // this.config.aaData = this.config.data = this.initData(1,this.config.lengthMenu[0]);
        this.config.aaData = this.config.data = this.config.allData;

        //渲染回调
        this.drawCallback();

        //初始化回调
        this.initComplete();

        this.isInit = false;

        $(this.config.elem).DataTable(this.config);
    }

    /**
     * 头部初始化
     * */
        //表头渲染完成的回调试
    Ctable.fn.tableHeaderCallback = function(){
        var that = this;
        var _config = that.config;
        var btHeaderCallback = _config.btHeaderCallback;
        if(_config.headerCallback === undefined){
            _config.headerCallback = function(thead, data, start, end, display){
                if( _config.checkbox &&  _config.mulselect ){
                    if( !$(thead).find('th').eq(0).find('input').length){ //表单中没有全选框，就添加上
                        $(thead).find('th').eq(0).html( '<input type="checkbox" lay-skin="primary" lay-filter ="checkboxsfilter" class="checkAll">').addClass('headerCheckAll');
                    }
                }
            }
        }
    }

    /**
     * 设置columns 针对复选框的
     * */

    Ctable.fn.setcolumns = function(){
        var that = this;
        if(this.config.checkbox){
            //给首列添加checkbox参数

            function setCheckbox(){
                var columns = that.config.columns;
                var checkboxObj = {
                    data:"id",
                    width:"28px",
                    className:"center ckbx28 extraCol",
                    orderable: false,
                    render: function (data, type, full, meta) {
                        var checkBoxHtml = '<div class="layui-unselect layui-form-checkbox" lay-skin="primary"><i class="layui-icon"></i></div>'
                        if( full.checkboxHide && full.checkboxHide =="1"){
                            return '<input type="checkbox"  class="checkchild trdisabled" lay-skin="primary" data-id="' + data + '"  disabled="" />';
                        }else{
                            return '<input type="checkbox"  class="checkchild" lay-skin="primary" data-id="' + data + '" lay-filter ="checkchild"/>'+ checkBoxHtml;
                        }
                    }
                };
                columns.unshift(checkboxObj);
                return columns
            }

            //给首列添加checkbox时columnDefs列数加1处理;
            function setcolumnDefs(){
                var columnDefs = that.config.columnDefs;
                if(columnDefs ===undefined) return;
                for(var i = 0; i< columnDefs.length;i++){
                    columnDefs[i].targets = columnDefs[i].targets + 1;
                }
                return columnDefs
            }

            this.config.columnDefs = setcolumnDefs();
            this.config.columns = setCheckbox();
        }
    }

    /**
     * 设置表格数据
     * @param page 页码  length 每页显示多少条
     *  @returns array 数据数组
    * */
    Ctable.fn.initData = function(data){
        var that = this;
        var newData = data;

        //判断数据对象reqId 是否一致，如果一致代表
        var isSameReqIdCount = 0;//具有相同reqId数量
        var oldReqId;//前一个reqId内容
        for(var i = 0;i < newData.length;i++){
            //添加初始化合并信息
            //初始化合并行信息数据，默认都是1，表示不合并,0代表行要删除，大于代表合并多少行
            newData[i].conflate = [];
            bt.each(that.config.mergeColumn,function(){
                newData[i].conflate.push(1);
            })

            if(i == 0){
                oldReqId = newData[i].reqId;
            }else{
                if(oldReqId == newData[i].reqId ){ //如果相同则加一
                    isSameReqIdCount += 1;
                    bt.each(newData[i].conflate,function(index,data){
                        newData[i].conflate[index] = 0;//值为1，代表不合并
                    })
                    if(i == (newData.length - 1)){ //当最后一条数还是相同时，更改合并信息
                        bt.each(newData[ i-isSameReqIdCount ].conflate,function(index,data){
                            newData[ i-isSameReqIdCount].conflate[index] = isSameReqIdCount + 1;//值为要加1
                        })
                    }
                }else{
                    //找到本组相同reqId的起启数据，将数据中的conflate设置为合并行的值
                    bt.each(newData[ i-isSameReqIdCount-1 ].conflate,function(index,data){
                        newData[ i-isSameReqIdCount-1 ].conflate[index] = isSameReqIdCount + 1;//值为要加1
                    })
                    isSameReqIdCount = 0;
                    oldReqId = newData[i].reqId;
                }
            }
        }
        //将添加了conflate合并信息的数据

        return newData;
    }

    //重绘方法
    Ctable.fn.drawCallback = function(){
        var that = this;
        var btDrawCallback = that.config.btDrawCallback;
        if(that.config.drawCallback ===undefined) {

            that.config.drawCallback = function (setting) {

                var api = this.api();
                // var data = this.api().data();
                var obj = api.table().page.info();

                var newData = that.initData(that.config.allData.slice(obj.start,obj.end));
                if(newData.length){
                    //遍历列 有三条列需要进行合并操作
                    for (var i = 0; i < that.config.mergeColumn.length; i++) {
                        //对每列进行遍历，依据merge 中的数据进行列合并

                        this.api().column(that.config.mergeColumn[i]).nodes().each(function (cell, j) {


                            if( j >= obj.start && j < obj.end){
                                var dataIndex = j - obj.start;
                                var rowspan = newData[dataIndex].conflate[i];
                                if (rowspan > 1) {
                                    $(cell).attr('rowspan', rowspan)
                                }
                                if (rowspan == 0) {
                                    $(cell).remove();
                                }
                            }

                        });
                    }
                }

                that.doSumData(api)

                if(btDrawCallback !==undefined && typeof(btDrawCallback) === "function"){
                    btDrawCallback(setting);
                }

                $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();

                $(that.config.elem).parents('.dataTables_wrapper').find('.sorting_asc.sorting_disabled').removeClass('sorting_asc sorting_disabled');
            }
        }
    }

    //表格初始化初始化
    Ctable.fn.initComplete = function(){
        var that = this;
        var btInitComplete = that.config.btInitComplete;
        if(that.config.initComplete ===undefined){
            that.config.initComplete = function(setting){
                if(that.config.checkbox){
                    // 行点点击事件
                    that.clickChildCheckBox(setting);
                    //全选
                    that.checkAll();
                }
                form.render('checkbox');

                //新的分页
                if(that.config.paging){
                    var tableWrapper = $(that.config.elem).parents('.dataTables_wrapper');
                    that.jumpPage(tableWrapper,setting);
                }
            }
        }
    }


    Ctable.fn.clickChildCheckBox = function(setting,josn){
        var _that = this, _config = this.config, elem = _config.elem;
        form.on('checkbox(checkchild)',function(data){
            var rowIndex = data.othis.parents('tr').index();
            var oThat = $(elem).find('tbody tr:eq('+rowIndex+')');
            var that = oThat[0];
            var table = new $.fn.dataTable.Api( setting )

            //点击禁用行时，直接return
            if(oThat.find("td:first-child input.trdisabled").length == 1 || oThat.hasClass('btsummary')){
                // layer.msg('此行信息不能选择');
                return;
            }

            //设置行选中方法
            _that.rowSelected(oThat);

            //点击右固定列时，判断是否有禁用行
            var rightFixedWrapper = oThat.parents('.DTFC_RightWrapper');
            if( rightFixedWrapper.length ){
                var rowIndex = oThat.index();
                var tr = $(_config.elem).find('tbody tr:eq('+rowIndex+')');
                if(tr.find("td:first-child input.trdisabled").length == 1){
                    // layer.msg('此行信息不能选择');
                    return;
                }
            }
            //点击回调方法
            var btClickRowCallback = _config.btClickRowCallback;
            if(typeof(btClickRowCallback) === "function"){
                var data = table.rows(that).data();
                var dataArr = [];
                var selectData = data[0];
                if(oThat.hasClass('selected') ){
                    if(selectData){
                        selectData.isSelect = true;
                    }
                }else{
                    if(selectData){
                        selectData.isSelect = false;
                    }
                }
                dataArr.push(selectData);
                btClickRowCallback(dataArr);
            }

            if(_config.summary){ //汇总
                var selectDataArr = _that.getSelectData(elem);
                _that.selectSumData(selectDataArr,$(elem));
            }
            form.render('checkbox');
        })

    }

    //在固定列时，设置固定列的选中样式
    Ctable.fn.rowSelected = function(elem){
        var that = this;
        var rowIndex = elem.index();
        var mulselect = that.config.mulselect;
        var tables = elem.parents('.dataTables_wrapper').find('table');
        var selectedStyle = that.selectedStyle;
        for(var i = 0;i < tables.length;i++){
            var clickTr = $(tables[i]).find("tbody tr:eq("+rowIndex+") ");
            if(!clickTr.length){
                continue;
            }
            that.toggleSelectedClass(clickTr);
            if(!mulselect){
                var trs = clickTr.siblings("tr").removeClass(selectedStyle);
                trs.find("input.checkchild:checked").prop("checked",false);
            }
            if( that.config.checkbox && clickTr.find("td:first-child input").length != 0){
                clickTr.find('input[type="checkbox"]').prop("checked",clickTr.hasClass("selected"));
            }
        }
        //根据选中的内容，切换全选状态
        if(that.config.checkbox){
            checkAll($(that.config.elem));
        }
    }

    function checkAll(elem) {
        var checked = elem.find("input.checkchild:not('.trdisabled')").length == $(elem).find("input.checkchild:checked").length ? true : false;
        elem.parents('.dataTables_scrollBody').siblings('.dataTables_scrollHead').find('table thead th input.checkAll ').prop('checked',checked);
        var leftWrapper = elem.parents('.dataTables_scroll').siblings('.DTFC_LeftWrapper');
        if(leftWrapper.length){
            leftWrapper.find('.DTFC_LeftHeadWrapper table thead th  input.checkAll').prop('checked',checked);
        }else{
            elem.find('thead th  input.checkAll').prop('checked',checked);
        }
    }

    Ctable.fn.checkAll = function(){
        var _that = this;
        var _config = _that.config;
        form.on('checkbox(checkboxsfilter)',function(data){
            $(_that.config.elem).find("input.checkchild:not('.trdisabled')").prop('checked',this.checked);
            if(this.checked){
                $(_that.config.elem).find("input.checkchild:not('.trdisabled')").parents('tr').addClass('selected');
            }else{
                $(_that.config.elem).find("input.checkchild:not('.trdisabled')").parents('tr').removeClass('selected');
            }
            var data = $.fn.dataTable.Api(_that.config.elem).rows('.selected').data()
            if(_config.summary){
                _that.selectSumData(data.splice(0,data.length),$(_that.config.elem));
            }
            form.render('checkbox');
        })

    }

    //切换选中的className方法
    Ctable.fn.toggleSelectedClass = function(elem){
        elem.toggleClass(this.selectedStyle)
    };

    //页面数据汇总处理
    Ctable.prototype.doSumData = function(api){
        var _config = this.config,that = this;
        //添加汇总html
        if(_config.summary && typeof(_config.summary) ==='object'){
            var summary = _config.summary;
            var $table = $(_config.elem);
            var api = api;
            var money = bt.money;
            var options = {
                decimal:summary.format !== undefined ? 2:summary.format
            };
            var groupColumn = summary.groupColumn;//分类汇总字段列
            var sumList = summary.sumList;
            var position = summary.position;
            var convertMoney = summary.convertMoney;//万元或者元参数
            var natualCurrency = summary.natualCurrency;//将结果转换成本位货参数
            var colsNamesArr = api.columns().dataSrc();//拿到每一列的数据名称;
            for(var k = 0 ; k < sumList.length; k++) {//更新sumlist汇总列清单列索引
                //列托动后，刷新列的index
                for (var a = 0; a < colsNamesArr.length; a++) {
                    if (sumList[k].dataName == colsNamesArr[a]) {
                        sumList[k].colNum = a;
                    }
                }
            }
            if(!groupColumn){ //非分类汇总
                var sumHtml = that.getSumHtml([],api);
                if(position != undefined && position == 'top'){
                    $table.find("tbody").prepend(sumHtml);
                }else{
                    $table.find("tbody").append(sumHtml);
                }
                bt.each(sumList,function(i,v){
                    var total,pageTotal,valueForPage,valueForselected,valueForAll;
                    if(!v.expression){
                        total = api
                            .column( v.colNum )
                            .data()
                            .reduce( function (a, b) {
                                return intVal(a) + intVal(b);
                            }, 0 );

                        pageTotal = api
                            .column( v.colNum, { page: 'current'} )
                            .data()
                            .reduce( function (a, b) {
                                return intVal(a) + intVal(b);
                            }, 0 );
                        i = 0;
                        //金额转换成万元或者元
                        if(typeof(convertMoney) === 'function'){
                            pageTotal = money.convertMoney(pageTotal,convertMoney());
                            total = money.convertMoney(total,convertMoney());
                        }else if(typeof(convertMoney) === 'string'){
                            pageTotal = money.convertMoney(pageTotal,convertMoney);
                            total = money.convertMoney(total,convertMoney);
                        }
                        if(v.suffix =='%'){//百分比时，不能大于100
                            valueForPage = valueForPage > 100 ?100:valueForPage;
                            valueForselected = valueForselected > 100 ?100:valueForPage;
                            valueForAll = valueForAll > 100 ?100:valueForPage;
                        }
                        if( v.money != false){ //非货币汇总不需要格式化
                            valueForPage = money.format(pageTotal,options);
                            valueForselected = money.format(0,options);
                            valueForAll = money.format(total,options);
                        }else{
                            valueForPage = pageTotal;
                            valueForselected = 0;
                            valueForAll =total;
                        }
                        //others:'%'//‰千分百分汇总
                        if( v.suffix ){ //如果有特殊要求后缀
                            valueForPage = valueForPage + v.suffix;
                            valueForselected = valueForselected + v.suffix;
                            valueForAll = valueForAll + v.suffix;
                        }
                        $table.find("tbody td.pageSum"+v.dataName + i).html(valueForPage).attr('title',valueForPage);//添加title属性
                        $table.find("tbody td.selectSum"+v.dataName + i).html(money.format(0,options)).attr('title',valueForselected);
                        $table.find("tbody td.allSum"+v.dataName + i).html(money.format(total,options)).attr('title',valueForAll);
                    }else{ //自定义汇总数据
                        var sumObj = {};
                        var tds = $table.find("tbody tr.pageSum0").find('.sumtd');
                        $.each(tds,function(){
                            var name = $(this).data('sum-name');
                            sumObj[name] = $(this).text();
                        })
                        valueForPage = v.expression(sumObj);//自定义
                        $table.find("tbody tr.pageSum0").find('.'+v.dataName).text(valueForPage).attr('title',valueForPage)
                    }

                })
            }else{//分类汇总
                var currenPageData = api.rows().data();
                var groupColumnIndex;
                for(var i = 0 ; i < colsNamesArr.length; i++){
                    if(colsNamesArr[i] == groupColumn){
                        groupColumnIndex = i;
                    }
                }
                var nodes = api.columns(groupColumnIndex).nodes();
                var groupColText = [];//分尖汇总列文本
                for( var i = 0; i < nodes[0].length;i++){
                    groupColText.push($(nodes[0][i]).text())
                }

                var groupColData = api.columns(groupColumnIndex).data();//分类汇总页的数据名字
                //数组去重,得到需要汇总的定段
                groupColData = doDouble(groupColData[0]);
                groupColText = doDouble(groupColText)
                that.groupSummaryName = groupColData;
                var sumHtml = that.getSumHtml(groupColText,api);//生成计算html
                if(position != undefined && position == 'top'){
                    $table.find("tbody").prepend(sumHtml);
                }else{
                    $table.find("tbody").append(sumHtml);
                }
                for(var k = 0 ; k < sumList.length; k++){//按照sumlist汇总列清单进行遍历
                    var courtName = sumList[k].dataName;//计算列的字段名
                    var courtIndex = sumList[k].colNum;//计算列的索引
                    var colFormat = sumList[k].format;//当前列数据格式
                    if(!sumList[k].expression){ //非自定义汇总数据
                        var numFormat;
                        if(colFormat !== undefined){
                            numFormat = {
                                decimal:colFormat
                            };
                        }else{
                            numFormat = options
                        }
                        var natualCurrencySum = 0;//本位币计算结果初始化为0
                        //遍历获取分类数值对象
                        var  sumObj = getGroupData(groupColumn,courtName,currenPageData,groupColData,$table,courtIndex);//每一列的数据
                        //分类汇总
                        for(var key in sumObj){
                            var sum = sumObj[key].reduce( function (a, b) {
                                return intVal(a) + intVal(b);
                            }, 0 );
                            //金额转换成万元或者元
                            if(typeof(convertMoney) === 'function'){
                                sum = money.convertMoney(sum,convertMoney());
                            }else if(typeof(convertMoney) === 'string'){
                                sum = money.convertMoney(sum,convertMoney);
                            }
                            //本位币转换：将日元或美元转换成人民币
                            if( natualCurrency && typeof(natualCurrency) === 'function'){
                                var natual = natualCurrency();
                                if(natual){
                                    var natualCurrencyFlag = true;//标识是否需要本位币换算;
                                    for(var i = 0;i<natual.length;i++){
                                        var item = natual[i];
                                        if(key == item.currency){
                                            var rate = item.rate;
                                            if(rate){
                                                natualCurrencyFlag = false;//由于已经换算了本位币，所以标识重置为false;
                                                natualCurrencySum += money.mul(sum,rate);
                                            }
                                            break;
                                        }
                                    }
                                    if(natualCurrencyFlag){//不用换本位币的直接相加
                                        natualCurrencySum += sum;
                                    }
                                }
                            }
                            for(var i = 0;i < groupColData.length;i++){
                                if(key == groupColData[i]){ //按照i进行列赋值
                                    var sumForPage,sumForSelected;
                                    if(!sumList[k].expression){
                                        if(sumList[k].suffix =='%'){//百分比时，不能大于100
                                            sumForPage = sumForPage > 100 ?100:sumForPage;
                                            sumForSelected = sumForSelected > 100 ?100:sumForSelected;
                                        }
                                        if(sumList[k].money != false){
                                            sumForPage =  money.format(sum,numFormat);
                                            sumForSelected = money.format(0,numFormat);
                                        }else{
                                            sumForPage = sum;
                                            sumForSelected = 0;
                                        }
                                        if( sumList[k].suffix ){ //如果有特殊要求后缀
                                            sumForPage = sumForPage + sumList[k].suffix;
                                            sumForSelected = sumForSelected + sumList[k].suffix;
                                        }
                                        $table.find("tbody td.pageSum"+courtName + i).html(sumForPage).attr('title',sumForPage);//添加title属性
                                        $table.find("tbody td.selectSum"+courtName + i).html(sumForSelected).attr('title',sumForSelected);//添加title属性
                                    }
                                }
                            }
                            //给本位币内容赋值
                            var sumForCurrency = money.format(natualCurrencySum,numFormat);
                            $table.find("tbody td.natualCurrency"+courtName).html(sumForCurrency).attr('title',sumForCurrency);
                        }
                    }else{ //自定义汇总内容
                        for(var i = 0;i < groupColData.length;i++){
                            var sumObj = {};
                            var tds = $table.find('tbody .pageSum'+i).find('.sumtd');
                            $.each(tds,function(){
                                var name = $(this).data('sum-name');
                                sumObj[name] = $(this).text();
                            })
                            var sumForPage = sumList[k].expression(sumObj);//根据自定义回调方法返回值
                            $table.find("tbody").find('.pageSum'+i).find('.'+courtName).text(sumForPage).attr('title',sumForPage)
                        }
                    }
                }
            }
        }
    }

    //数组去重方法
    function doDouble(arr) {
        var res = [arr[0]];
        for(var i= 1;i < arr.length;i++){
            var isOnly = false;
            for( var j = 0;j< res.length;j++ ){
                if( arr[i] === res[j]){
                    isOnly = false;
                    break;
                }else{
                    isOnly = true;
                }
            }
            if(isOnly){
                res.push(arr[i]);
            }
        }
        return res;
    }
    //转number
    function intVal( i ) {
        if( $.isNumeric(i) ){ //其它非数字字符不处理
            return i-0;
        }else{
            return 0;
        }
    }

    /**
     表格数据分类汇总
     @param {String} groupColName 数据的分类字段名称
     @param {String} keyName 汇总数值字段名称
     @param {Array} data 数据
     @param {Array} groupArray 分类汇总字段数组
     return {Object} 分类整理的数组对象
     */
    function getGroupData(groupColName,keyName,data,groupArray,$table,colIndex) {
        var sumObj = {};//分类存贮数值
        //遍历获取分类数值
        for(var i = 0 ; i < data.length; i++){
            var rowData = data[i];
            for(var j = 0 ; j < groupArray.length;j++){
                var groupKey = groupArray[j];
                if(i === 0){
                    sumObj[groupKey] = [];
                }
                if(rowData[groupColName] == groupKey){
                    //var num = $table.find('tbody tr:eq('+i+')').find('td:eq('+colIndex+')').text();
                    var num = rowData[keyName];//解决隐藏列汇总的问题
                    sumObj[groupKey].push(num);
//                    sumObj[groupKey].push(rowData[keyName])
                }
            }
        }
        return sumObj;
    }
    //设置汇总的html
    Ctable.prototype.getSumHtml = function(groupNameArray,api){
        var that = this;
        var _config = that.config,
            summary = _config.summary,
            sumList = summary.sumList,
            groupColumn = summary.groupColumn;//分类汇总参数
        var natualCurrency = summary.natualCurrency;//将结果转换成本位货参数
        var position = summary.position;
        var groupText = '';//分类汇总文本
        var thsLen =  $(_config.elem).find('thead tr th').length;
        var footHtml ='',trhtml ='',sumArr=[];
        var isCheckbox = _config.checkbox; //是否第一列为chekbox
        var sumTextCol = summary.sumTextCol;//汇总文本列信息
        var colsNamesArr = api.columns().dataSrc();
        var colsVisible = api.columns().visible();//获取列的显示状态
        if(summary.selectSum){
            sumArr.push("selectSum")
        }
        if(summary.pageSum){
            sumArr.push("pageSum")
        }
        if(summary.allSum){
            sumArr.push("allSum")
        }
        if(!groupColumn){
            groupNameArray = [0] //代表无分类汇总
        }

        //遍历生成汇总html
        for(var j = 0;j< groupNameArray.length;j++){
            groupText = groupNameArray[j] ? groupNameArray[j] :'';

            bt.each(sumArr,function(i,v){
                var isPageSum = false;//分页小计标识，应用于导出所有的分页小计
                trhtml +='<tr class="btsummary  '+v+j+'">';
                //for(var i = 0; i < colsNamesArr.length;i++){
                for(var i = 0; i < colsVisible.length;i++){
                    var flag= true;
                    if( sumTextCol === undefined && !isCheckbox && i === 0 || sumTextCol !== undefined && i == sumTextCol || sumTextCol === undefined && isCheckbox && i === 1){
                        var sumName ='';
                        if( v =="selectSum"){
                            sumName =(groupText+bt.lang("table.datatable.subtotalSelected"));//"已选小计"
                        }
                        else if (v =="pageSum"){
                            sumName = (groupText+bt.lang("table.datatable.subtotal"));//"小计"
                            isPageSum = true;//设置为true
                        }else if(v =="allSum"){
                            sumName = (groupText + bt.lang("common.button.collect"));//"汇总"
                        }
                        if(colsVisible[i]){ //汇总文本列出现隐藏,也要给加上display　none
                            trhtml +='<td align="center" >'+sumName+'</td>';
                        }else{
                            trhtml +='<td align="center" style="display:none">'+sumName+'</td>';
                        }
                    }else{
                        for(var a = 0;a < sumList.length;a++){
                            if( sumList[a].colNum && sumList[a].colNum === i){
                                var vName = sumList[a].money == false ? v + sumList[a].dataName +j+' '+ 'center':v + sumList[a].dataName +j+' '+ 'textRight';
                                sumList[a].suffix = sumList[a].suffix || '';
                                sumList[a].money = sumList[a].money == false ? false:true;
                                var expression = sumList[a].expression?'expression':'';
                                if(colsVisible[i]){
                                    trhtml +='<td class=" sumtd '+ vName + ' '+ sumList[a].dataName +'"  data-suffix="'+sumList[a].suffix+'"  data-money="'+sumList[a].money+'" data-sum-name="'+sumList[a].dataName+'" data-sum-callback="'+expression+'"';
                                }else{//汇总列被隐藏时的问题
                                    trhtml +='<td class="sumtd '+ vName + ' '+ sumList[a].dataName +'" style="display:none;" data-suffix="'+sumList[a].suffix+'"  data-money="'+sumList[a].money+'" data-sum-name="'+sumList[a].dataName+'" data-sum-callback="'+expression+'"';
                                }

                                if(!isPageSum){
                                    trhtml +=  '></td>'
                                }else { //如果有分页汇总，在html中添加分页汇总的内容，应用于导入所有数据时的汇总
                                    trhtml +=  'data-sumpage-text ="'+groupText+'"></td>'
                                }
                                flag = false;
                                break;
                            }
                        }
                        if(flag){
                            if( isCheckbox && i === 0 ){
                                trhtml +='<td style="width: 26px;height:18px;" class="extraCol"></td>';
                            }else{
                                if(colsVisible[i]){
                                    if( colsNamesArr[i] == "null" || colsNamesArr[i] === null){
                                        trhtml +='<td class="extraCol"></td>';
                                    }else{
                                        trhtml +='<td></td>';
                                    }
                                }else{
                                    if(colsNamesArr[i]){
                                        trhtml +='<td style="display:none;"></td>';
                                    }else{
                                        trhtml +='<td style="display:none;" class="extraCol"></td>';
                                    }
                                }
                            }
                        }
                    }
                }
                trhtml +='</tr>';
            })
            footHtml += trhtml;
            trhtml = '';
        }
        //生成本位币html
        if( natualCurrency && typeof(natualCurrency) === 'function' && natualCurrency()){
            var currencyHtml ='<tr class="btsummary">';
            for(var i = 0; i < thsLen;i++){
                var flag= true;
                if(i === 1){
                    currencyHtml +='<td>'+bt.lang("table.datatable.subtotalBaseCurrency")+'</td>';//本位币小计
                }else{
                    bt.each(sumList,function(index,item){
                        var  vName = 'natualCurrency'+item.dataName+' '+ 'textRight';
                        if(i == item.colNum ){
                            currencyHtml +='<td class="'+vName+'"></td>';
                            flag = false;
                        }
                    })
                    if(flag){
                        currencyHtml +='<td></td>';
                    }
                }
            }
            currencyHtml +='</tr>';
            if(position && position =='top'){
                footHtml = currencyHtml + footHtml;
            }else{
                footHtml += currencyHtml;
            }
        }
        return footHtml;
    }

    Ctable.prototype.getTableHtml = function(){
        var that = this;
        var _config = that.config;
        var html = $(_config.elem).html();
        return html;
    }

    //设置选中数据汇总的方法
    Ctable.prototype.selectSumData = function(data,$table){
        var sumList = this.config.summary.sumList,that=this;
        var groupColumn = this.config.summary.groupColumn;
        var money = bt.money;
        var options = that.config.summary.format;
        var outerData = this.config.summary.outerData;
        var convertMoney = this.config.summary.convertMoney;//金额转换成万元或者元
        if(!groupColumn){
            bt.each(sumList,function(index,item){
                var vName = item.dataName;
                var sum = 0;
                if(outerData){ //当配置了此参数，代表汇总数据需要从页面上取才可以汇总，因为需要汇总的数据进行了二次加工。
                    var col = item.colNum;
                    var trs = $table.find('.selected');
                    bt.each(trs,function (i,tr) { //选中小计
                        var td = $(tr).find('td:eq('+col+')');
                        if( td.find('input').length || td.find('.changeful').length){ //changeful样式的标签内的内容是多变的，计算时要重新获取
                            var outerValue = 0;
                            if(td.find('input').length){
                                outerValue = td.find('input').val();
                            }
                            else if(td.find('.changeful').length){
                                outerValue = td.find('.changeful').text();
                            }
                            sum = sum + ( intVal(money.toNumber(outerValue))-0 );//对数据进行格式化
                        }else{
                            sum = sum + ( intVal(td.text())-0 );//对数据进行格式化
                        }
                    })
                    //本页总计
                    var allTrs = $table.find('tr:not(.btsummary)');
                    var pageSum = 0;
                    var isRefreshPageSum = false;
                    bt.each(allTrs,function (i,tr) { //选中小计
                        var td = $(tr).find('td:eq('+col+')');
                        if( td.find('input').length || td.find('.changeful').length ){

                            isRefreshPageSum = true;
                            var outerSumValue = 0;
                            if(td.find('input').length){
                                outerSumValue = td.find('input').val();
                            }
                            else if(td.find('.changeful').length){
                                outerSumValue = td.find('.changeful').text();
                            }
                            pageSum = pageSum + ( intVal(money.toNumber(outerSumValue))-0 );//对数据进行格式化
                        }else{
                            pageSum = pageSum + ( intVal(td.text())-0 );//对数据进行格式化
                        }
                    })
                    if(isRefreshPageSum){
                        pageSum = money.format(sum,options);
                        $(that.config.elem).find("tbody .pageSum"+item.dataName+'0').text(pageSum).attr('title',pageSum);
                    }
                }else{
                    bt.each(data,function(i,v){
                        sum = sum + (v[vName] -0);
                    })
                }
                index = 0;
                //金额转换成万元或者元
                if(typeof(convertMoney) === 'function'){
                    sum = money.convertMoney(sum,convertMoney());
                }else if(typeof(convertMoney) === 'string'){
                    sum = money.convertMoney(sum,convertMoney);
                }
                var sumForSelect =  money.format(sum,options);
                $(that.config.elem).find("tbody .selectSum"+item.dataName+index).text(sumForSelect).attr('title',sumForSelect);
                var rightFixedCol = $(that.config.elem).parents('.dataTables_scroll').siblings('.DTFC_RightWrapper');
                if(rightFixedCol.length > 0){
                    rightFixedCol.find('.DTFC_RightBodyWrapper').find("tbody .selectSum"+item.dataName+index).text(sumForSelect).attr('title',sumForSelect);
                }
            })
        }else{
            var groupArray = that.groupSummaryName;
            var courtName = sumList[0].dataName;//计算列的字段名
            var courtIndex = sumList[0].colNum;//计算列的索引
            //获取选择数据分类
            var sumObj = getGroupData(groupColumn,courtName,data,groupArray,$table,courtIndex);
            //分类汇总
            for(var key in sumObj){
                var sum = sumObj[key].reduce( function (a, b) {
                    return intVal(a) + intVal(b);
                }, 0 );
                //金额转换成万元或者元
                if(typeof(convertMoney) === 'function'){
                    sum = money.convertMoney(sum,convertMoney());
                }else if(typeof(convertMoney) === 'string'){
                    sum = money.convertMoney(sum,convertMoney);
                }
                for(var i = 0;i < groupArray.length;i++){
                    if(key == groupArray[i]){
                        var valueForSelect = money.format(sum,options);
                        $(that.config.elem).find("tbody .selectSum"+ courtName + i ).html(valueForSelect).attr('title',valueForSelect);
                    }
                }
            }
        }
    }

    Ctable.prototype.jumpPage = function($table,setting){
        var that = this;
        var toolDiv = $table.find('.jump_page');
        var html = '<span class="table_input_text">'+bt.lang("table.datatable.pageTo")+'</span><input type="text" class="table_input_page "/>&nbsp;&nbsp'+bt.lang("common.table.page"); //跳至 页
        toolDiv.html(html);
        keyupPageNumber(toolDiv);
        enterJumpPage(new $.fn.dataTable.Api( setting ),toolDiv);
    }

    //控制只能输入正整数
    function keyupPageNumber(toolsDiv){
        //$('.dataTables_wrapper .wrapper input').on('keyup',function(){
        toolsDiv.find('input').on('keyup',function(){
            if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}
        }).on('afterpaste',function(){
            if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')};
        })

    }
    //按回车键页面跳转
    function enterJumpPage(table,toolsDiv){
        toolsDiv.on('keydown','.table_input_page',function(e){
            e = event || windw.event;
            if(e.keyCode ===13){
                var val = parseInt($(this).val());
                if(val>=0){
                    if(val > table.page.info().pages){//大于最大页码时，清空页码
                        $(this).val('')
                    }else{
                        val--
                        table.page(val).draw('page');
                    }
                }
                $(this).blur();
            }
        })
    }

    /**
     * 获取选中数据方法
     * id 表格id  #tbl
     * */
    Ctable.fn.getSelectData = function(id){
        var that = this;
        var selectDataArr = [];
        if(that.config.elem == id){

            var selectedTrs =  $(that.config.elem).find('tbody tr.selected');

            bt.each(selectedTrs,function(){
                var colIndex = $(this).index();
                selectDataArr.push(that.config.data[colIndex]);
            })
        }
        return selectDataArr
    }

    var table = new Ctable();

    var obj = {
        init:function(options){ //组件初始化方法
            table.set(options);
            table.render();
            return this;
        },
        getSelectData:function(id){ //获取选中数据
            return table.getSelectData(id)
        }
    }

    exports('customTable', obj);
}).addcss('../../../css/datatable.css','datatablecss');
layui.addcss('../../../css/customTable.css','customtablecss');
