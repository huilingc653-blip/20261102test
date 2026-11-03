//公共类库模块管理
layui.config({
    base: basePath + '/static/',
    debug: false,
}).extend({ //设定组件别名
    btab: 'js/btab',					//tab切换
    selectTree: 'js/selectTree',		//下拉树，by周亮
    linkage: 'js/linkage',				//通用联动，by周亮
    treeTable: 'js/treeTable',			//树表，by周亮
    netCorp:'js/netCorp',               //网点单位联动，by周亮
    navtab: 'js/oldJs/navtab',
    dateForm: 'js/dateForm',
    pickList: 'js/pickList',             //下拉多选 by周亮
    selectCallback: 'js/selectCallback', //公共选择回调
    selectCallback1: 'js/selectCallback1', //公共选择回调


    moneynum: 'js/moneynum',			//数字千分位格式化,by刘勇军
    select: 'js/select',//单选下拉框，by刘勇军
    checkboxs: 'js/checkboxs',			//复选框单选多选，by刘勇军
    datatable: 'js/oldJs/datatable',			//datatable，by刘勇军
    btdate: 'js/btdate',					//时间控件，by刘勇军
    bank: 'js/bank',						//银行格式化控件，by刘勇军
    btSearch: 'js/btSearch',
    datatableSearchForm: 'js/datatableSearchForm',
    capNumber: 'js/capNumber',          //数字大写转化 by刘勇军
    bttable: 'js/bttable',              //普通表格 by刘勇军
    selectTable: 'js/selectTable',      //普通表格 by刘勇军
    freezetable: 'js/freezetable',      //冻结行列 by刘勇军
    verify: 'js/verify',                //验证 by刘勇军
    mulselect: 'js/oldJs/mulselect',          //多选 by刘勇军
    submit: 'js/submit',          //提交 by刘勇军
    page: 'js/page',          //提交 by刘勇军
    pageType: 'js/biz/pageType',          //提交 by刘勇军
    choImport: 'js/choImport',           //理财登记导入组件
    dtlImport: 'js/dtlImport',           //流水管理导入组件
    relaypage: 'js/relaypage',           //最新版 分页  袁波

    //业务组件
    accebillInfo: 'js/biz/accebillInfo',             //票据信息，by 郑刚
    accebillView: 'js/biz/accebillView',             //根据票据流水ID查看票据信息，by 郑刚
    corp: 'js/biz/corp',                             //网点、单位，by 周亮
    corp1: 'js/biz/corp1',                             //网点、单位
    users: 'js/biz/user',                            //用户by 郑刚
    dictGroup: 'js/biz/dictGroup',                       //字典组类型by 郑刚
    dicts: 'js/biz/dict',                            //字典，by 周亮
    bankInfo: 'js/biz/bankInfo',                     //联行号，by 周亮
    bankType: 'js/biz/bankType',                     //银行类别，by 周亮
    externalCorpBankAcc: 'js/biz/externalCorpBankAcc',//客商帐号，by 周亮
    externalCorp: 'js/biz/externalCorp',//单位客商选择组件，by zhengg 20200713
    inputExternalCorpBankAcc: 'js/biz/inputExternalCorpBankAcc',//下拉客商帐号，by 刘勇军
    accInFoByCorp: 'js/biz/accInFoByCorp',		    //内部账号,by 王智星
    region: 'js/biz/region',                        //省市级联，by 周亮
    personnelDetail: 'js/biz/personnelDetail',      //职工号，by 左宇
    bisFinanceExc: 'js/biz/bisFinanceExc',          //理财指令，by 黄茜
    choInnerFinance: 'js/biz/choInnerFinance',          //内部理财存款，by 黄茜
    fexProductInfo: 'js/biz/fexProductInfo',          //外汇产品选择，by 黄茜
    fexProductView: 'js/biz/fexProductView',          //外汇产品查看，by 黄茜
    externalCorpInfo: 'js/biz/externalCorpInfo',          //客商选择(多选)，by 黄茜
    externalCorpView: 'js/biz/externalCorpView',          //客商查看，by 黄茜
    bankAccInfo: 'js/biz/bankAccInfo',          //账号选择(多选)，by 黄茜
    bankAccView: 'js/biz/bankAccView',          //账号查看，by 黄茜
    moneyType: 'js/biz/moneyType',                  //货币，by 刘勇军
    rateType: 'js/biz/rateType',                    //银行汇率，by 刘勇军
    selectType: 'js/biz/selectType',                //下拉框类型，by 刘勇军
    dateType: 'js/biz/dateType',                    //时间类型，by 刘勇军
    accountTable: 'js/biz/accountTable',			// 账户表格,by 左宇
    customTable:'js/biz/customTable',                //自定义表格 by 刘勇军
    itemType: 'js/biz/itemType',                    //业务组件：by zhangxb 预算科目
    radioType: 'js/biz/radioType',                  //单选框组件：单选 by 刘勇军
    selectTableType: 'js/oldJs/biz/selectTableType',      //下拉框组件：单选 by 刘勇军
    selectTableType2: 'js/oldJs/biz/selectTableType2',      //下拉表组件：单选 by 刘勇军
    printType: 'js/biz/printType',                  //打印组件：打印 by 刘勇军
    ratioType: 'js/biz/ratioType',                  //比例组件：打印 by 刘勇军
    choProduct: 'js/biz/choProduct',                //理财产品，by 沈聪
    choProductType: 'js/biz/choProductType',        //理财产品类型，by 吴显辰
    choProductRenewal: 'js/biz/choProductRenewal',  //理财赎回，by 欧阳杰
    mulCorp:'js/biz/mulCorp',                       //单位下拉树，by 周鹏，只包含网点下面的一级单位
    mulAccInfo:'js/biz/mulAccInfo',                 //多选内部账户列表业务组件，by 周鹏，内部账户为有效
    interAccInfo:'js/biz/interAccInfo',                 //多选内部账户列表业务组件，by 陈飞，内部账户为有效，账户类别固定为（银行活期或企业活期）
    guaPledgeInfo:'js/biz/guaPledgeInfo',       //多选抵质押物信息列表业务组件，by 符殊彬
    mulBankAccInfo:'js/biz/mulBankAccInfo',         //多选银行账户列表业务组件，by 周鹏，银行账号没有删除且有效的账号
    bondAccInfo:'js/biz/bondAccInfo',               //保证金账户组件 by 卢向勇
    guaContract:'js/biz/guaContract',               //担保合同组件 by 卢向勇
    rasShop:'js/biz/rasShop',               //营收稽核门店信息业务组件 by 郑刚
    swiftInfo: 'js/biz/swiftInfo',                  //境外银行行号信息组件，by 秦雷
    externalCorpBankAccFex: 'js/biz/externalCorpBankAccFex', //国外客商信息组件，by 王智星
    conSelect: 'js/biz/conSelect',             //合同选择控件，by 郑刚
    conView: 'js/biz/conView',             //根据合同ID查看合同信息，by 郑刚
    conType: 'js/biz/conType',             //合同类型业务组件，by 郑刚
    billPoolif: 'js/biz/billPoolifSelect',           //票据池接口业务组件，by 郑鸿杰
    billPool: 'js/biz/billPoolSelect',               //票据池业务组件，by 郑鸿杰
    billPoolCorp: 'js/biz/billPoolCorp',                    //票据池单位业务组件，by 汪增异
    interestCorp: 'js/biz/interestCorp',                    //个别计息业务组件，by 谢金花
    busType: 'js/biz/busType',                    //票据池业务类型，by 曹金勇
    worldMap: 'js/biz/worldMap',                    //世界地图，by 刘勇军
    capitalMap: 'js/biz/capitalMap',                    //资金地图，by 刘勇军　替代世界地图
    domesticAndOverseasBank: 'js/biz/domesticAndOverseasBank',             //开户行和境外银行组件，by zuoy
    mulSelectUserPage: 'js/biz/mulSelectUserPage',             //银行账号多选组件，by 秦雷
    mulViewUserPage: 'js/biz/mulViewUserPage',             //银行账号多选查询，by 秦雷
    mulSelectBankFinancePage: 'js/biz/mulSelectBankFinancePage',             //银行理财多选组件，by 秦雷
    mulViewBankFinancePage: 'js/biz/mulViewBankFinancePage',             //银行理财多选查询，by 秦雷
    cell: 'js/biz/cell',                    //管理单元组件，by 汪增异
    choProductRedeem: 'js/biz/choProductRedeem',                  //理财赎回信息组件，by 秦雷
    organtypeCorp: 'js/biz/organtypeCorp',                    //组织架构单位业务组件，by 郑鸿杰
    bankAccAbroad:'js/biz/bankAccAbroad',               //根据境内查银行账号 by 郑西娜
    bankAccDomestic:'js/biz/bankAccDomestic',               //根据境外查银行账号 by 郑西娜
    fcAccInfo:'js/biz/fcAccInfo',                           //计息内部账号 by 王智星
    accountInfo:'js/biz/accountInfo',                           //内部账号选择 by 黄茜
    erpMulBankAccInfo:'js/biz/erpMulBankAccInfo',                        //ERP对账多选银行账号 by 祁继鸿
    accebillIntellect: 'js/biz/accebillIntellect',             //智能选票，by 郑鸿杰
    bisAccDtl: 'js/biz/bisAccDtl',             //银行账户流水，by 姜维

    //常用js方法
    money: 'js/bmethod/money',
    date: 'js/bmethod/date',
    uuid: 'js/bmethod/uuid',
    tools: 'js/bmethod/tools',
    treeGrid: 'js/treeGrid',                //新树形表格 by周亮
    tableGrid: 'js/tableGrid',              //新表格 by周亮
    ligerTreeTable: 'js/ligerTreeTable',              //新表格 by周亮
    datatable2: 'js/oldJs/datatable2',                  //固定列可编辑的表格，by 刘勇军



    //重置layui组件
    // btForm:'js/btform',

    //第三方组件
    ztree: 'plugins/ztree/ztree.core',
    ztreeCheck: 'plugins/ztree/ztree.excheck',
    ztreeHide: 'plugins/ztree/ztree.HideNodes',
    ztreeEdit: 'plugins/ztree/ztree.exedit',
    treeTableCore: 'plugins/treeTable/treeTableCore',
    jqGrid: 'plugins/jqGrid/jqgrid.core',
    gridExtend: 'plugins/jqGrid/jqgrid.extend',
    jqueryUI: 'plugins/jquery-ui/jquery-ui',
    jqColor: 'plugins/jquery-color/jquery.color',
    datatablecore: 'plugins/oldDatatables/datatables.core',
    datatableColReorder: 'plugins/oldDatatables/dataTables.colReorder',
    datatableColVis: 'plugins/oldDatatables/dataTables.colVis',
    datatableButtons: 'plugins/oldDatatables/dataTables.buttons',
    datatableRowsGroup: 'plugins/oldDatatables/dataTables.rowsGroup',
    formatnumber: 'plugins/format-number/formatnumber',//数字格式化
    colResize: 'plugins/oldDatatables/colResizable-1.5.min',//拖拽
    // colResize: 'plugins/datatables/dataTables.colResize',//拖拽
    // fixedColumns: 'plugins/datatables/dataTables.fixedColumns.min',
    fixedColumns: 'plugins/oldDatatables/dataTables.fixedColumns',
    fixedHeader:'plugins/oldDatatables/dataTables.fixedHeader',
    cronGen: 'plugins/cron/cronGen',//cron表达式
    datatableSelect: 'plugins/oldDatatables/dataTables.select.min',//选择
    lodop: 'plugins/Lodop/Lodop',//lodop打印
    datatablePrint: 'plugins/oldDatatables/datatables.print',//打印
    html5: 'plugins/oldDatatables/buttons.html5',//datatables 按钮文件
    jszip: 'plugins/oldDatatables/jszip',//datatables 导出文件 JSZip v3.1.3 - A Javascript class for generating and reading zip files

    calendar: 'plugins/calendar/calendar',//日历
    btUpload: 'plugins/upload/upload',//上传组件
    btImport: 'plugins/import/import',//导入组件
    // fixHeader: 'plugins/fixHeader/jquery.freezeheader',//固定表
    ligerGrid:'plugins/liger/ligerGrid',//liger插件，用于树表格的冻结
    ligerBase:'plugins/liger/base',//liger插件，用于树表格的冻结
    toLigerTree:'plugins/liger/toLigerTree',//普通树表格转为支持冻结列的树表格
    ligerResizable:'plugins/liger/ligerResizable',//冻结树表格托宽功能
    freezeTable:'v3.2/plugins/freezeTable/freezeTable', //冻结表格头及列
    accebillInfoPay: 'js/biz/accebillInfoPay',
    accebillInfoReceive: 'js/biz/accebillInfoReceive',
    crScoringDataSource: 'js/biz/crScoringDataSource',//获取评分指标的数据源组件
    budgetFbsItem: 'js/biz/budgetFbsItem',
});
