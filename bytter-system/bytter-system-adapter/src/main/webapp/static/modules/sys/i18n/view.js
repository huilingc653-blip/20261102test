bt.use(['form', 'jquery'], function () {
    var form = bt.form(), layer = bt.layer, $ = bt.jquery;

    var i18nItemList = [];
    var keyId = $("#keyIdElement").val();
    var type = $("#typeElement").val();
    if (type !== "0") {
        // 0，基础数据；1，基础业务数据。
        // 默认基础业务数据。
        type = 1;
    }

    getI18NList();
    addLocaleChangeListener();
    addDeleteI18NItemListener();
    addContextChangeListener();

    /**
     * 获取I19N列表
     */
    function getI18NList() {
        //let url = `${basePath}/sys/i18n/list?type=${type}&keyId=${keyId}`;
        var url = basePath + '/sys/i18n/list?type=' + type + '&keyId=' + keyId;
        $.get(url, function (result) {
            i18nItemList = result;
            renderData(i18nItemList);
        }, "JSON");
    }

    /**
     * 拼接行HTML代码
     * @param index
     * @param i18nItem
     * @returns {string}
     */
    function getI18NItemHtml(index, i18nItem) {
        var i18nItemHtml = '';
        i18nItemHtml += '<tr data-index="' + index + '">';
        i18nItemHtml += '<td class="text-center">' + index + '</td>';
        i18nItemHtml += '<td>';
        i18nItemHtml += '<select name="locale" lay-filter="localeFilter" data-index="' + index + '">';
        i18nItemHtml += '<option value="zh_CN">' + bt.lang('sys.lang.chinese') + '</option>';
        i18nItemHtml += '<option value="en_US">' + bt.lang('sys.lang.english') + '</option>';
        i18nItemHtml += '</select>';
        i18nItemHtml += '</td>';

        i18nItemHtml += '<td class="text-center">';
        i18nItemHtml += '<input type="text" style="width: 100%;" name="context" data-index="' + index + '" value="' + i18nItem.context + '" class="layui-input">';
        i18nItemHtml += '</td>';
        i18nItemHtml += '<td class="text-center">';
        i18nItemHtml += '<button data-toggle="deleteMultiLangItemElement" class="table-btn layui-btn layui-btn-small layui-btn-danger">' + bt.lang('common.button.delete') + '</button>';
        i18nItemHtml += '</td>';
        i18nItemHtml += '</tr>';


        return i18nItemHtml;
    }

    /**
     * 渲染数据
     * @param dataList
     */
    function renderData(dataList) {
        var html = '';
        if (dataList.length > 0) {
            for (var index = 0; index < dataList.length; index++) {
                var element = dataList[index];
                html += getI18NItemHtml(index + 1, element);
            }
        } else {
            html = '<tr style="height:250px;"><td style="text-align:center;" colspan="4" class="empty-td"><img src="'+ basePath +'/static/v3.2/images/pic_blank.png" class="pic-bank"><div class="empty-text">'+bt.lang('common.msg.temporarily_no_data')+'</div></td></tr>';
        }
        $("#i18nItemListContainerElement").html(html);
        // 设置选中语种
        var localeElementList = document.getElementsByName("locale");
        for (var index = 0; index < localeElementList.length; index++) {
            var localeElement = localeElementList[index];
            $(localeElement).val(i18nItemList[index].locale);
        }
        //刷新select选择框渲染
        form.render('select');
    }

    /**
     * 监听语种下拉框变化事件
     */
    function addLocaleChangeListener() {
        form.on('select(localeFilter)', function (data) {
            // data{ elem :节点DOM对象 ,othis:'',value:'选中的值'}
            var selectedLocale = data.value;
            var localeElement = $(data.elem);
            var index = localeElement.data("index");
            if (index >= 0 && index < i18nItemList.length) {
                i18nItemList[index].locale = selectedLocale;
            }
        });

    }

    /**
     * 添加显示值变化事件监听，记录输入的显示值
     */
    function addContextChangeListener() {

        $(document).on('change', 'input[name="context"]', function () {
            var contextElement = $(this);
            var context = contextElement.val();
            var index = contextElement.data("index");
            if (index >= 0 && index < i18nItemList.length) {
                i18nItemList[index].context = context;
            }
        });
    }


    /**
     * 监听删除语种事件
     */
    function addDeleteI18NItemListener() {

        $(document).on('click', 'button[data-toggle="deleteMultiLangItemElement"]', function () {
            //获取子节点的父级元素
            var parentElement = $(this).parents("tr");
            var layer = parent ? parent.layer : layer;
            layer.confirm(bt.lang('sys.region.deleteInfo'), {icon: 3}, function () {
                var locale = parentElement.find("option:selected").val();
                $.ajax({
                    type: 'GET',
                    url: basePath + "/sys/i18n/delete",
                    dataType: "json",
                    data: {type: type, keyId: keyId, langCode: locale},
                    success: function (result) {
                        parentElement.detach();
                        layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            });
        });
    }


    /**
     * 添加语种事件
     */
    $("#addI18NItemElement").click(function () {
        var index = i18nItemList.length;
        var addI18NItem = {
            locale: 'zh_CN',
            context: ''
        };
        i18nItemList.push(addI18NItem);
        var i18nItemHtml = getI18NItemHtml(index, addI18NItem);
        // 如果列表为空，则使用html()方法
        if (index > 0) {
            $("#i18nItemListContainerElement").append(i18nItemHtml);
        } else {
            $("#i18nItemListContainerElement").html(i18nItemHtml);
        }

        //刷新select选择框渲染
        form.render('select');
    });


    //监听提交
    form.on('submit(fieldMultiLangSubmit)', function (data) {
        var localeElementList = document.getElementsByName("locale");
        var langContextElementList = document.getElementsByName("context");

        var itemLength = localeElementList.length;
        var i18NItemList = [];
        var localeSet = {};
        for (var index = 0; index < itemLength; index++) {
            var localeElement = $(localeElementList[index]);
            var locale = localeElement.val();
            if (localeSet[locale]) {
                parent.layer.alert(bt.lang("sys.i18n.alert.langRepeat"), {icon: 2});
                return;
            }
            localeSet[locale] = locale;
            var langContextElement = $(langContextElementList[index]);
            var langContext = langContextElement.val();
            if (!langContext) {
                parent.layer.alert(bt.lang("sys.resource.contentShow"), {icon: 2});
                langContextElement.focus();
                return;
            }
            i18NItemList.push({
                keyId: keyId,
                locale: locale,
                context: langContext
            });
        }

        $.ajax({
            type: 'POST',
            url: basePath + "/sys/i18n/saveOrUpdate",
            // data:'JSON',
            contentType: "application/json",
            data: JSON.stringify({type: type, dataList: i18NItemList}),
            success: function (result) {
                var resObj = result;
                // if (resObj.success) {
                //     parent.layer.closeAll();
                // }
                parent.layer.alert(resObj.msg, {icon: resObj.success ? 1 : 2});
            }
        });
        return false;
    });


});
