
/*!
 * HTML5 export buttons for Buttons and DataTables.
 * 2016 SpryMedia Ltd - datatables.net/license
 *
 * FileSaver.js (1.3.3) - MIT license
 * Copyright © 2016 Eli Grey - http://eligrey.com
 */
bt.define(["jquery","jszip",'money','layer'],function(exports){
	var jQuery=bt.jquery;
	(function( factory ){
		// console.log(window.JSZip)
		if ( typeof define === 'function' && define.amd ) {
			// AMD
			define( ['jquery', 'datatables.net', 'datatables.net-buttons'], function ( $ ) {
				return factory( $, window, document );
			} );
		}
		else if ( typeof exports === 'object' ) {
			// CommonJS
			module.exports = function (root, $, jszip, pdfmake) {
				if ( ! root ) {
					root = window;
				}

				if ( ! $ || ! $.fn.dataTable ) {
					$ = require('datatables.net')(root, $).$;
				}

				if ( ! $.fn.dataTable.Buttons ) {
					require('datatables.net-buttons')(root, $);
				}

				return factory( $, root, root.document, jszip, pdfmake );
			};
		}
		else {
			// Browser
			factory( jQuery, window, document );
		}
	}(function( $, window, document, jszip, pdfmake, undefined ) {
		var DataTable = $.fn.dataTable;

		// Allow the constructor to pass in JSZip and PDFMake from external requires.
		// Otherwise, use globally defined variables, if they are available.
		function _jsZip () {
			return jszip || window.JSZip;
		}
		function _pdfMake () {
			return pdfmake || window.pdfMake;
		}


		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * FileSaver.js dependency
         */

		/*jslint bitwise: true, indent: 4, laxbreak: true, laxcomma: true, smarttabs: true, plusplus: true */

		var _saveAs = (function(view) {
			// IE <10 is explicitly unsupported
			if (typeof view === "undefined" || typeof navigator !== "undefined" && /MSIE [1-9]\./.test(navigator.userAgent)) {
				return;
			}
			var
				doc = view.document
				// only get URL when necessary in case Blob.js hasn't overridden it yet
				, get_URL = function() {
					return view.URL || view.webkitURL || view;
				}
				, save_link = doc.createElementNS("http://www.w3.org/1999/xhtml", "a")
				, can_use_save_link = "download" in save_link
				, click = function(node) {
					var event = new MouseEvent("click");
					node.dispatchEvent(event);
				}
				, is_safari = /constructor/i.test(view.HTMLElement) || view.safari
				, is_chrome_ios =/CriOS\/[\d]+/.test(navigator.userAgent)
				, throw_outside = function(ex) {
					(view.setImmediate || view.setTimeout)(function() {
						throw ex;
					}, 0);
				}
				, force_saveable_type = "application/octet-stream"
				// the Blob API is fundamentally broken as there is no "downloadfinished" event to subscribe to
				, arbitrary_revoke_timeout = 1000 * 40 // in ms
				, revoke = function(file) {
					var revoker = function() {
						if (typeof file === "string") { // file is an object URL
							get_URL().revokeObjectURL(file);
						} else { // file is a File
							file.remove();
						}
					};
					setTimeout(revoker, arbitrary_revoke_timeout);
				}
				, dispatch = function(filesaver, event_types, event) {
					event_types = [].concat(event_types);
					var i = event_types.length;
					while (i--) {
						var listener = filesaver["on" + event_types[i]];
						if (typeof listener === "function") {
							try {
								listener.call(filesaver, event || filesaver);
							} catch (ex) {
								throw_outside(ex);
							}
						}
					}
				}
				, auto_bom = function(blob) {
					// prepend BOM for UTF-8 XML and text/* types (including HTML)
					// note: your browser will automatically convert UTF-16 U+FEFF to EF BB BF
					if (/^\s*(?:text\/\S*|application\/xml|\S*\/\S*\+xml)\s*;.*charset\s*=\s*utf-8/i.test(blob.type)) {
						return new Blob([String.fromCharCode(0xFEFF), blob], {type: blob.type});
					}
					return blob;
				}
				, FileSaver = function(blob, name, no_auto_bom) {
					if (!no_auto_bom) {
						blob = auto_bom(blob);
					}
					// First try a.download, then web filesystem, then object URLs
					var
						filesaver = this
						, type = blob.type
						, force = type === force_saveable_type
						, object_url
						, dispatch_all = function() {
							dispatch(filesaver, "writestart progress write writeend".split(" "));
						}
						// on any filesys errors revert to saving with object URLs
						, fs_error = function() {
							if ((is_chrome_ios || (force && is_safari)) && view.FileReader) {
								// Safari doesn't allow downloading of blob urls
								var reader = new FileReader();
								reader.onloadend = function() {
									var url = is_chrome_ios ? reader.result : reader.result.replace(/^data:[^;]*;/, 'data:attachment/file;');
									var popup = view.open(url, '_blank');
									if(!popup) view.location.href = url;
									url=undefined; // release reference before dispatching
									filesaver.readyState = filesaver.DONE;
									dispatch_all();
								};
								reader.readAsDataURL(blob);
								filesaver.readyState = filesaver.INIT;
								return;
							}
							// don't create more object URLs than needed
							if (!object_url) {
								object_url = get_URL().createObjectURL(blob);
							}
							if (force) {
								view.location.href = object_url;
							} else {
								var opened = view.open(object_url, "_blank");
								if (!opened) {
									// Apple does not allow window.open, see https://developer.apple.com/library/safari/documentation/Tools/Conceptual/SafariExtensionGuide/WorkingwithWindowsandTabs/WorkingwithWindowsandTabs.html
									view.location.href = object_url;
								}
							}
							filesaver.readyState = filesaver.DONE;
							dispatch_all();
							revoke(object_url);
						}
					;
					filesaver.readyState = filesaver.INIT;

					if (can_use_save_link) {
						object_url = get_URL().createObjectURL(blob);
						setTimeout(function() {
							save_link.href = object_url;
							save_link.download = name;
							click(save_link);
							dispatch_all();
							revoke(object_url);
							filesaver.readyState = filesaver.DONE;
						});
						return;
					}

					fs_error();
				}
				, FS_proto = FileSaver.prototype
				, saveAs = function(blob, name, no_auto_bom) {
					return new FileSaver(blob, name || blob.name || "download", no_auto_bom);
				}
			;
			// IE 10+ (native saveAs)
			if (typeof navigator !== "undefined" && navigator.msSaveOrOpenBlob) {
				return function(blob, name, no_auto_bom) {
					name = name || blob.name || "download";

					if (!no_auto_bom) {
						blob = auto_bom(blob);
					}
					return navigator.msSaveOrOpenBlob(blob, name);
				};
			}

			FS_proto.abort = function(){};
			FS_proto.readyState = FS_proto.INIT = 0;
			FS_proto.WRITING = 1;
			FS_proto.DONE = 2;

			FS_proto.error =
				FS_proto.onwritestart =
					FS_proto.onprogress =
						FS_proto.onwrite =
							FS_proto.onabort =
								FS_proto.onerror =
									FS_proto.onwriteend =
										null;

			return saveAs;
		}(
			typeof self !== "undefined" && self
			|| typeof window !== "undefined" && window
			|| this.content
		));


// Expose file saver on the DataTables API. Can't attach to `DataTables.Buttons`
// since this file can be loaded before Button's core!
		DataTable.fileSave = _saveAs;


		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * Local (private) functions
         */

		/**
		 * Get the sheet name for Excel exports.
		 *
		 * @param {object}	config Button configuration
		 */
		var _sheetname = function ( config )
		{
			var sheetName = 'Sheet1';

			if ( config.sheetName ) {
				sheetName = config.sheetName.replace(/[\[\]\*\/\\\?\:]/g, '');
			}

			return sheetName;
		};

		/**
		 * Get the newline character(s)
		 *
		 * @param {object}	config Button configuration
		 * @return {string}				Newline character
		 */
		var _newLine = function ( config )
		{
			return config.newline ?
				config.newline :
				navigator.userAgent.match(/Windows/) ?
					'\r\n' :
					'\n';
		};

		/**
		 * Combine the data from the `buttons.exportData` method into a string that
		 * will be used in the export file.
		 *
		 * @param	{DataTable.Api} dt		 DataTables API instance
		 * @param	{object}				config Button configuration
		 * @return {object}							 The data to export
		 */
		var _exportData = function ( dt, config )
		{
			var newLine = _newLine( config );
			var data = dt.buttons.exportData( config.exportOptions );
			var boundary = config.fieldBoundary;
			var separator = config.fieldSeparator;
			var reBoundary = new RegExp( boundary, 'g' );
			var escapeChar = config.escapeChar !== undefined ?
				config.escapeChar :
				'\\';
			var join = function ( a ) {
				var s = '';

				// If there is a field boundary, then we might need to escape it in
				// the source data
				for ( var i=0, ien=a.length ; i<ien ; i++ ) {
					if ( i > 0 ) {
						s += separator;
					}

					s += boundary ?
						boundary + ('' + a[i]).replace( reBoundary, escapeChar+boundary ) + boundary :
						a[i];
				}

				return s;
			};

			var header = config.header ? join( data.header )+newLine : '';
			var footer = config.footer && data.footer ? newLine+join( data.footer ) : '';
			var body = [];

			for ( var i=0, ien=data.body.length ; i<ien ; i++ ) {
				body.push( join( data.body[i] ) );
			}

			return {
				str: header + body.join( newLine ) + footer,
				rows: body.length
			};
		};

		/**
		 * Older versions of Safari (prior to tech preview 18) don't support the
		 * download option required.
		 *
		 * @return {Boolean} `true` if old Safari
		 */
		var _isDuffSafari = function ()
		{
			var safari = navigator.userAgent.indexOf('Safari') !== -1 &&
				navigator.userAgent.indexOf('Chrome') === -1 &&
				navigator.userAgent.indexOf('Opera') === -1;

			if ( ! safari ) {
				return false;
			}

			var version = navigator.userAgent.match( /AppleWebKit\/(\d+\.\d+)/ );
			if ( version && version.length > 1 && version[1]*1 < 603.1 ) {
				return true;
			}

			return false;
		};

		/**
		 * Convert from numeric position to letter for column names in Excel
		 * @param  {int} n Column number
		 * @return {string} Column letter(s) name
		 */
		function createCellPos( n ){
			var ordA = 'A'.charCodeAt(0);
			var ordZ = 'Z'.charCodeAt(0);
			var len = ordZ - ordA + 1;
			var s = "";

			while( n >= 0 ) {
				s = String.fromCharCode(n % len + ordA) + s;
				n = Math.floor(n / len) - 1;
			}

			return s;
		}

		try {
			var _serialiser = new XMLSerializer();
			var _ieExcel;
		}
		catch (t) {}

		/**
		 * Recursively add XML files from an object's structure to a ZIP file. This
		 * allows the XSLX file to be easily defined with an object's structure matching
		 * the files structure.
		 *
		 * @param {JSZip} zip ZIP package
		 * @param {object} obj Object to add (recursive)
		 */
		function _addToZip( zip, obj ) {
			if ( _ieExcel === undefined ) {
				// Detect if we are dealing with IE's _awful_ serialiser by seeing if it
				// drop attributes
				_ieExcel = _serialiser
					.serializeToString(
						$.parseXML( excelStrings['xl/worksheets/sheet1.xml'] )
					)
					.indexOf( 'xmlns:r' ) === -1;
			}

			$.each( obj, function ( name, val ) {
				if ( $.isPlainObject( val ) ) {
					var newDir = zip.folder( name );
					_addToZip( newDir, val );
				}
				else {
					if ( _ieExcel ) {
						// IE's XML serialiser will drop some name space attributes from
						// from the root node, so we need to save them. Do this by
						// replacing the namespace nodes with a regular attribute that
						// we convert back when serialised. Edge does not have this
						// issue
						var worksheet = val.childNodes[0];
						var i, ien;
						var attrs = [];

						for ( i=worksheet.attributes.length-1 ; i>=0 ; i-- ) {
							var attrName = worksheet.attributes[i].nodeName;
							var attrValue = worksheet.attributes[i].nodeValue;

							if ( attrName.indexOf( ':' ) !== -1 ) {
								attrs.push( { name: attrName, value: attrValue } );

								worksheet.removeAttribute( attrName );
							}
						}

						for ( i=0, ien=attrs.length ; i<ien ; i++ ) {
							var attr = val.createAttribute( attrs[i].name.replace( ':', '_dt_b_namespace_token_' ) );
							attr.value = attrs[i].value;
							worksheet.setAttributeNode( attr );
						}
					}

					var str = _serialiser.serializeToString(val);

					// Fix IE's XML
					if ( _ieExcel ) {
						// IE doesn't include the XML declaration
						if ( str.indexOf( '<?xml' ) === -1 ) {
							str = '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'+str;
						}

						// Return namespace attributes to being as such
						str = str.replace( /_dt_b_namespace_token_/g, ':' );
					}

					// Safari, IE and Edge will put empty name space attributes onto
					// various elements making them useless. This strips them out
					str = str.replace( /<([^<>]*?) xmlns=""([^<>]*?)>/g, '<$1 $2>' );

					zip.file( name, str );
				}
			} );
		}

		/**
		 * Create an XML node and add any children, attributes, etc without needing to
		 * be verbose in the DOM.
		 *
		 * @param  {object} doc      XML document
		 * @param  {string} nodeName Node name
		 * @param  {object} opts     Options - can be `attr` (attributes), `children`
		 *   (child nodes) and `text` (text content)
		 * @return {node}            Created node
		 */
		function _createNode( doc, nodeName, opts ) {
			var tempNode = doc.createElement( nodeName );
			if ( opts ) {
				if ( opts.attr ) {
					$(tempNode).attr( opts.attr );
				}

				if ( opts.children ) {
					$.each( opts.children, function ( key, value ) {
						tempNode.appendChild( value );
					} );
				}

				if ( opts.text !== null && opts.text !== undefined ) {
					tempNode.appendChild( doc.createTextNode( opts.text ) );
				}
			}

			return tempNode;
		}
		function _byteLength(str){
			var byteLen = 0, len = str.length;
			if( !str ) return 0;
			for( var i=0; i<len; i++ )
				byteLen += str.charCodeAt(i) > 255 ? 2 : 1;
			return byteLen;
		}
		/**
		 * Get the width for an Excel column based on the contents of that column
		 * @param  {object} data Data for export
		 * @param  {int}    col  Column index
		 * @return {int}         Column width
		 */
		function _excelColWidth( data, col ) {
			var max = _byteLength(data.header[col]);//data.header[col].length;
			var len, lineSplit, str;

			if ( data.footer && data.footer[col].length > max ) {
				max = data.footer[col].length;
			}
			for ( var i=0, ien=data.body.length ; i<ien ; i++ ) {
				var point = data.body[i][col];
				str = point !== null && point !== undefined ?
					point.toString() :
					'';
				// If there is a newline character, workout the width of the column
				// based on the longest line in the string
				if ( str.indexOf('\n') !== -1 ) {
					lineSplit = str.split('\n');
					lineSplit.sort( function (a, b) {
						return b.length - a.length;
					} );
					len = _byteLength(lineSplit[0]);//lineSplit[0].length;
				}
				else {
					len = _byteLength(str);//str.length;
				}
				if ( len > max ) {
					max = len;
				}
				// Max width rather than having potentially massive column widths
				if ( max > 40 ) {
					return 52; // 40 * 1.3
				}
			}
			max *= 1.3;
			// And a min width
			//return max > 6 ? max*100/100 : 6;
			return max > 6 ? Math.floor(max * 100) / 100 : 6;

		}

// Excel - Pre-defined strings to build a basic XLSX file
		var excelStrings = {
			"_rels/.rels":
				'<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'+
				'<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">'+
				'<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>'+
				'</Relationships>',

			"xl/_rels/workbook.xml.rels":
				'<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'+
				'<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">'+
				'<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" Target="worksheets/sheet1.xml"/>'+
				'<Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>'+
				'</Relationships>',

			"[Content_Types].xml":
				'<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'+
				'<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">'+
				'<Default Extension="xml" ContentType="application/xml" />'+
				'<Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml" />'+
				'<Default Extension="jpeg" ContentType="image/jpeg" />'+
				'<Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml" />'+
				'<Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml" />'+
				'<Override PartName="/xl/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml" />'+
				'</Types>',

			"xl/workbook.xml":
				'<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'+
				'<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">'+
				'<fileVersion appName="xl" lastEdited="5" lowestEdited="5" rupBuild="24816"/>'+
				'<workbookPr showInkAnnotation="0" autoCompressPictures="0"/>'+
				'<bookViews>'+
				'<workbookView xWindow="0" yWindow="0" windowWidth="25600" windowHeight="19020" tabRatio="500"/>'+
				'</bookViews>'+
				'<sheets>'+
				'<sheet name="" sheetId="1" r:id="rId1"/>'+
				'</sheets>'+
				'</workbook>',

			"xl/worksheets/sheet1.xml":
				'<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'+
				'<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" mc:Ignorable="x14ac" xmlns:x14ac="http://schemas.microsoft.com/office/spreadsheetml/2009/9/ac">'+
				'<sheetData/>'+
				'<mergeCells count="0"/>'+
				'</worksheet>',

			"xl/styles.xml":
				'<?xml version="1.0" encoding="UTF-8"?>'+
				'<styleSheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" mc:Ignorable="x14ac" xmlns:x14ac="http://schemas.microsoft.com/office/spreadsheetml/2009/9/ac">'+
				'<numFmts count="6">'+
				'<numFmt numFmtId="164" formatCode="#,##0.00_-\ [$$-45C]"/>'+
				'<numFmt numFmtId="165" formatCode="&quot;£&quot;#,##0.00"/>'+
				'<numFmt numFmtId="166" formatCode="[$€-2]\ #,##0.00"/>'+
				'<numFmt numFmtId="167" formatCode="0.0%"/>'+
				'<numFmt numFmtId="168" formatCode="#,##0;(#,##0)"/>'+
				'<numFmt numFmtId="169" formatCode="#,##0.00;(#,##0.00)"/>'+
				'</numFmts>'+
				'<fonts count="5" x14ac:knownFonts="1">'+
				'<font>'+
				'<sz val="11" />'+
				'<name val="Calibri" />'+
				'</font>'+
				'<font>'+
				'<sz val="11" />'+
				'<name val="Calibri" />'+
				'<color rgb="FFFFFFFF" />'+
				'</font>'+
				'<font>'+
				'<sz val="11" />'+
				'<name val="Calibri" />'+
				'<b />'+
				'</font>'+
				'<font>'+
				'<sz val="11" />'+
				'<name val="Calibri" />'+
				'<i />'+
				'</font>'+
				'<font>'+
				'<sz val="11" />'+
				'<name val="Calibri" />'+
				'<u />'+
				'</font>'+
				'</fonts>'+
				'<fills count="6">'+
				'<fill>'+
				'<patternFill patternType="none" />'+
				'</fill>'+
				'<fill/>'+ // Excel appears to use this as a dotted background regardless of values
				'<fill>'+
				'<patternFill patternType="solid">'+
				'<fgColor rgb="FFD9D9D9" />'+
				'<bgColor indexed="64" />'+
				'</patternFill>'+
				'</fill>'+
				'<fill>'+
				'<patternFill patternType="solid">'+
				'<fgColor rgb="FFD99795" />'+
				'<bgColor indexed="64" />'+
				'</patternFill>'+
				'</fill>'+
				'<fill>'+
				'<patternFill patternType="solid">'+
				'<fgColor rgb="ffc6efce" />'+
				'<bgColor indexed="64" />'+
				'</patternFill>'+
				'</fill>'+
				'<fill>'+
				'<patternFill patternType="solid">'+
				'<fgColor rgb="ffc6cfef" />'+
				'<bgColor indexed="64" />'+
				'</patternFill>'+
				'</fill>'+
				'</fills>'+
				'<borders count="2">'+
				'<border>'+
				'<left />'+
				'<right />'+
				'<top />'+
				'<bottom />'+
				'<diagonal />'+
				'</border>'+
				'<border diagonalUp="false" diagonalDown="false">'+
				'<left style="thin">'+
				'<color auto="1" />'+
				'</left>'+
				'<right style="thin">'+
				'<color auto="1" />'+
				'</right>'+
				'<top style="thin">'+
				'<color auto="1" />'+
				'</top>'+
				'<bottom style="thin">'+
				'<color auto="1" />'+
				'</bottom>'+
				'<diagonal />'+
				'</border>'+
				'</borders>'+
				'<cellStyleXfs count="1">'+
				'<xf numFmtId="0" fontId="0" fillId="0" borderId="0" />'+
				'</cellStyleXfs>'+
				'<cellXfs count="67">'+
				'<xf numFmtId="0" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="1" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="2" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="3" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="4" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="0" fillId="2" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="1" fillId="2" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="2" fillId="2" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="3" fillId="2" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="4" fillId="2" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="0" fillId="3" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="1" fillId="3" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="2" fillId="3" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="3" fillId="3" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="4" fillId="3" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="0" fillId="4" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="1" fillId="4" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="2" fillId="4" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="3" fillId="4" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="4" fillId="4" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="0" fillId="5" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="1" fillId="5" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="2" fillId="5" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="3" fillId="5" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="4" fillId="5" borderId="0" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="0" fillId="0" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="1" fillId="0" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="2" fillId="0" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="3" fillId="0" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="4" fillId="0" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="0" fillId="2" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="1" fillId="2" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="2" fillId="2" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="3" fillId="2" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="4" fillId="2" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="0" fillId="3" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="1" fillId="3" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="2" fillId="3" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="3" fillId="3" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="4" fillId="3" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="0" fillId="4" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="1" fillId="4" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="2" fillId="4" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="3" fillId="4" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="4" fillId="4" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="0" fillId="5" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="1" fillId="5" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="2" fillId="5" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="3" fillId="5" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="4" fillId="5" borderId="1" applyFont="1" applyFill="1" applyBorder="1"/>'+
				'<xf numFmtId="0" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyAlignment="1">'+
				'<alignment horizontal="left"/>'+
				'</xf>'+
				'<xf numFmtId="0" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyAlignment="1">'+
				'<alignment horizontal="center"/>'+
				'</xf>'+
				'<xf numFmtId="0" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyAlignment="1">'+
				'<alignment horizontal="right"/>'+
				'</xf>'+
				'<xf numFmtId="0" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyAlignment="1">'+
				'<alignment horizontal="fill"/>'+
				'</xf>'+
				'<xf numFmtId="0" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyAlignment="1">'+
				'<alignment textRotation="90"/>'+
				'</xf>'+
				'<xf numFmtId="0" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyAlignment="1">'+
				'<alignment wrapText="1"/>'+
				'</xf>'+
				'<xf numFmtId="9"   fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyNumberFormat="1"/>'+
				'<xf numFmtId="164" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyNumberFormat="1"/>'+
				'<xf numFmtId="165" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyNumberFormat="1"/>'+
				'<xf numFmtId="166" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyNumberFormat="1"/>'+
				'<xf numFmtId="167" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyNumberFormat="1"/>'+
				'<xf numFmtId="168" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyNumberFormat="1"/>'+
				'<xf numFmtId="169" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyNumberFormat="1"/>'+
				'<xf numFmtId="3" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyNumberFormat="1"/>'+
				'<xf numFmtId="4" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyNumberFormat="1"/>'+
				'<xf numFmtId="1" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyNumberFormat="1"/>'+
				'<xf numFmtId="2" fontId="0" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyNumberFormat="1"/>'+

				'<xf numFmtId="0" fontId="2" fillId="0" borderId="0" applyFont="1" applyFill="1" applyBorder="1" xfId="0" applyAlignment="1">'+
				'<alignment horizontal="center" vertical= "center"/>'+
				'</xf>'+

				'</cellXfs>'+
				'<cellStyles count="1">'+
				'<cellStyle name="Normal" xfId="0" builtinId="0" />'+
				'</cellStyles>'+
				'<dxfs count="0" />'+
				'<tableStyles count="0" defaultTableStyle="TableStyleMedium9" defaultPivotStyle="PivotStyleMedium4" />'+
				'</styleSheet>'
		};
// Note we could use 3 `for` loops for the styles, but when gzipped there is
// virtually no difference in size, since the above can be easily compressed

// Pattern matching for special number formats. Perhaps this should be exposed
// via an API in future?
// Ref: section 3.8.30 - built in formatters in open spreadsheet
//   https://www.ecma-international.org/news/TC45_current_work/Office%20Open%20XML%20Part%204%20-%20Markup%20Language%20Reference.pdf
		var _excelSpecials = [
			//TODO:2017年11月23日17:22:49暂不处理百分号数据  直接单成字符串
			//{ match: /^\-?\d+\.\d%$/,       style: 60, fmt: function (d) { return d/100; } }, // Precent with d.p.
			//{ match: /^\-?\d+\.?\d*%$/,     style: 56, fmt: function (d) { return d/100; } }, // Percent
			{ match: /^\-?\$[\d,]+.?\d*$/,  style: 57 }, // Dollars
			{ match: /^\-?£[\d,]+.?\d*$/,   style: 58 }, // Pounds
			{ match: /^\-?€[\d,]+.?\d*$/,   style: 59 }, // Euros
			{ match: /^\-?\d+$/,            style: 65 }, // Numbers without thousand separators
			// { match: /^\-?\d+\.\d{2}$/,     style: 66 }, // Numbers 2 d.p. without thousands separators
			//解决导入有效数字不对的问题
			{ match: /^\-?\d+\.\d+$/,     style: 66 }, // Numbers 2 d.p. without thousands separators
			{ match: /^\([\d,]+\)$/,        style: 61, fmt: function (d) { return -1 * d.replace(/[\(\)]/g, ''); } },  // Negative numbers indicated by brackets
			{ match: /^\([\d,]+\.\d{2}\)$/, style: 62, fmt: function (d) { return -1 * d.replace(/[\(\)]/g, ''); } },  // Negative numbers indicated by brackets - 2d.p.
			{ match: /^\-?[\d,]+$/,         style: 63 }, // Numbers with thousand separators
			{ match: /^\-?[\d,]+\.\d{2}$/,  style: 64 }  // Numbers with 2 d.p. and thousands separators
		];



		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * Buttons
         */

//
// Copy to clipboard
//
		DataTable.ext.buttons.copyHtml5 = {
			className: 'buttons-copy buttons-html5',

			text: function ( dt ) {
				return dt.i18n( 'buttons.copy', 'Copy' );
			},

			action: function ( e, dt, button, config ) {
				this.processing( true );

				var that = this;
				var exportData = _exportData( dt, config );
				var info = dt.buttons.exportInfo( config );
				var newline = _newLine(config);
				var output = exportData.str;
				var hiddenDiv = $('<div/>')
					.css( {
						height: 1,
						width: 1,
						overflow: 'hidden',
						position: 'fixed',
						top: 0,
						left: 0
					} );

				if ( info.title ) {
					output = info.title + newline + newline + output;
				}

				if ( info.messageTop ) {
					output = info.messageTop + newline + newline + output;
				}

				if ( info.messageBottom ) {
					output = output + newline + newline + info.messageBottom;
				}

				if ( config.customize ) {
					output = config.customize( output, config );
				}

				var textarea = $('<textarea readonly/>')
					.val( output )
					.appendTo( hiddenDiv );

				// For browsers that support the copy execCommand, try to use it
				if ( document.queryCommandSupported('copy') ) {
					hiddenDiv.appendTo( dt.table().container() );
					textarea[0].focus();
					textarea[0].select();

					try {
						var successful = document.execCommand( 'copy' );
						hiddenDiv.remove();

						if (successful) {
							dt.buttons.info(
								dt.i18n( 'buttons.copyTitle', 'Copy to clipboard' ),
								dt.i18n( 'buttons.copySuccess', {
									1: 'Copied one row to clipboard',
									_: 'Copied %d rows to clipboard'
								}, exportData.rows ),
								2000
							);

							this.processing( false );
							return;
						}
					}
					catch (t) {}
				}

				// Otherwise we show the text box and instruct the user to use it
				var message = $('<span>'+dt.i18n( 'buttons.copyKeys',
					'Press <i>ctrl</i> or <i>\u2318</i> + <i>C</i> to copy the table data<br>to your system clipboard.<br><br>'+
					'To cancel, click this message or press escape.' )+'</span>'
				)
					.append( hiddenDiv );

				dt.buttons.info( dt.i18n( 'buttons.copyTitle', 'Copy to clipboard' ), message, 0 );

				// Select the text so when the user activates their system clipboard
				// it will copy that text
				textarea[0].focus();
				textarea[0].select();

				// Event to hide the message when the user is done
				var container = $(message).closest('.dt-button-info');
				var close = function () {
					container.off( 'click.buttons-copy' );
					$(document).off( '.buttons-copy' );
					dt.buttons.info( false );
				};

				container.on( 'click.buttons-copy', close );
				$(document)
					.on( 'keydown.buttons-copy', function (e) {
						if ( e.keyCode === 27 ) { // esc
							close();
							that.processing( false );
						}
					} )
					.on( 'copy.buttons-copy cut.buttons-copy', function () {
						close();
						that.processing( false );
					} );
			},

			exportOptions: {},

			fieldSeparator: '\t',

			fieldBoundary: '',

			header: true,

			footer: false,

			title: '*',

			messageTop: '*',

			messageBottom: '*'
		};

//
// CSV export
//
		DataTable.ext.buttons.csvHtml5 = {
			bom: false,

			className: 'buttons-csv buttons-html5',

			available: function () {
				return window.FileReader !== undefined && window.Blob;
			},

			text: function ( dt ) {
				return dt.i18n( 'buttons.csv', 'CSV' );
			},

			action: function ( e, dt, button, config ) {
				this.processing( true );
				// Set the text
				var output = _exportData( dt, config ).str;
				var info = dt.buttons.exportInfo(config);
				var charset = config.charset;

				if ( config.customize ) {
					output = config.customize( output, config );
				}

				if ( charset !== false ) {
					if ( ! charset ) {
						charset = document.characterSet || document.charset;
					}

					if ( charset ) {
						charset = ';charset='+charset;
					}
				}
				else {
					charset = '';
				}

				if ( config.bom ) {
					output = '\ufeff' + output;
				}

				_saveAs(
					new Blob( [output], {type: 'text/csv'+charset} ),
					info.filename,
					true
				);

				this.processing( false );
			},

			filename: '*',

			extension: '.csv',

			exportOptions: {},

			fieldSeparator: ',',

			fieldBoundary: '"',

			escapeChar: '"',

			charset: null,

			header: true,

			footer: false
		};

//
// Excel (xlsx) export
//
		DataTable.ext.buttons.excelHtml5 = {
			className: 'buttons-excel buttons-html5',

			available: function () {
				return window.FileReader !== undefined && _jsZip() !== undefined && ! _isDuffSafari() && _serialiser;
			},

			text: function ( dt ) {
				return dt.i18n( 'buttons.excel', 'Excel' );
			},

			action: function ( e, dt, button, config ) {

				this.processing( true );
				var that = this;
				var rowPos = 0;
				var getXml = function ( type ) {
					var str = excelStrings[ type ];
					//str = str.replace( /xmlns:/g, 'xmlns_' ).replace( /mc:/g, 'mc_' );
					return $.parseXML( str );
				};
				var rels = getXml('xl/worksheets/sheet1.xml');
				var relsGet = rels.getElementsByTagName( "sheetData" )[0];

				var xlsx = {
					_rels: {
						".rels": getXml('_rels/.rels')
					},
					xl: {
						_rels: {
							"workbook.xml.rels": getXml('xl/_rels/workbook.xml.rels')
						},
						"workbook.xml": getXml('xl/workbook.xml'),
						"styles.xml": getXml('xl/styles.xml'),
						"worksheets": {
							"sheet1.xml": rels
						}
					},
					"[Content_Types].xml": getXml('[Content_Types].xml')
				};
				var data = dt.buttons.exportData( config.exportOptions );
				// 导出选中行
				if(config.exportOptions.type === 'selectedExport' && config.exportOptions.callback && typeof config.exportOptions.callback === 'function'){
					var callbackData = config.exportOptions.callback(dt,data.body,that.processing);
					// 如果未选择数据，将会返回false 直接退出
					if(!callbackData){
						this.processing( false );
						return;
					}
					data.body = callbackData;

				}
				if(config.exportOptions.type==="otherExport"){ // 其他导出功能
					if ( typeof config.exportOptions.callback == 'function'){
						config.exportOptions.callback(that.processing)
						return;
					}
				}

				//增加当前页的方法调用
				if(config.exportOptions.type==="current"){
					var d = $.extend(JSON.parse(dt.ajax.params()),{length:5000});
					if ( typeof config.exportOptions.customExportCurrent == 'function'){
						var dataInfo = {
							title:'',
							fileName:'',
							total:dt.page.info().recordsTotal,
							columns:[],
							dataTablesInput:JSON.parse(dt.ajax.params())
						};
						if (dataInfo.total==0){
							layer.alert(bt.lang('common.msg.temporarily_no_data'));
							that.processing(false);
							return
						};
						var params = JSON.parse(dt.ajax.params());
						var currentColumns = $.extend([],dt.settings()[0].aoColumns);
						var fields = data.fields,newColumns =[],summaryCols=[],col={},paramCols=[];
						var rowElem = dt.rows().nodes();
						var summaryELem = $(rowElem).siblings('.btsummary');
						bt.each(summaryELem,function () {
							if (this.className.indexOf('pageSum') != -1){
								bt.each($(this).find(".sumtd"),function () {
									summaryCols.push($(this).data("sum-name"))
								})
							}
						})
						for (var i =0,length=fields.length;i < length;i++){
							for (var k =0,len=currentColumns.length;k < len;k++){
								if (currentColumns[k].data == fields[i]){
									var itemObj={};
									col={};
									itemObj.title = col.title= data.header[i];//表头
									itemObj.align = col.align= data.align[i]?data.align[i]:'left';//对齐方式
									itemObj.idx = col.idx= i;//列索引，从０开始
									itemObj.visible = col.visible= true;　//是否显示
									itemObj.data = itemObj.name = col.name=fields[i];
									itemObj.width = col.width= currentColumns[k].sWidth
									for (var b =0;b < summaryCols.length; b++){
										if (currentColumns[k].data == summaryCols[b]){
											itemObj.isPagesum = col.isPagesum = true;
										}
									}
									newColumns.push(col);
									paramCols.push(itemObj);
									break
								}
							}
						}
						params.columns = paramCols;
						dataInfo.columns = newColumns;
						var exportInfoObj = dt.buttons.exportInfo( config );
						params.filename = dataInfo.fileName = exportInfoObj.filename.split('.')[0];
						params.title = dataInfo.title = exportInfoObj.title;
						//参数that.processing为启动或者关闭加载动画方法，that.processing（true）代表开启动画；that.processing(false)代表关闭动
						// config.exportOptions.customExportAll(params,that.processing,dataInfo);
						config.exportOptions.customExportCurrent(params,that.processing,dataInfo);
						$(".dt-button-collection").hide();
						return
					}
				}

				if(config.exportOptions.type==="all"){//导出1000数据
					//修改为5000　liuyj
					var d = $.extend(JSON.parse(dt.ajax.params()),{length:5000});
					// 如果customExportAll 为function时，代表自定义导出所有
					if ( typeof config.exportOptions.customExportAll == 'function'){
						var dataInfo = {
							title:'',
							fileName:'',
							total:dt.page.info().recordsTotal,
							columns:[],
							dataTablesInput:JSON.parse(dt.ajax.params())
						};
						if (dataInfo.total==0){
							layer.alert(bt.lang('common.msg.temporarily_no_data'));
							that.processing(false);
							return
						};
						var params = JSON.parse(dt.ajax.params());
						var currentColumns = $.extend([],dt.settings()[0].aoColumns);
						var fields = data.fields,newColumns =[],summaryCols=[],col={},paramCols=[];
						var rowElem = dt.rows().nodes();
						var summaryELem = $(rowElem).siblings('.btsummary');
						bt.each(summaryELem,function () {
							if (this.className.indexOf('pageSum') != -1){
								bt.each($(this).find(".sumtd"),function () {
									summaryCols.push($(this).data("sum-name"))
								})
							}
						})
						for (var i =0,length=fields.length;i < length;i++){
							for (var k =0,len=currentColumns.length;k < len;k++){
								if (currentColumns[k].data == fields[i]){
									var itemObj={};
									col={};
									itemObj.title = col.title= data.header[i];//表头
									itemObj.align = col.align= data.align[i]?data.align[i]:'left';//对齐方式
									itemObj.idx = col.idx= i;//列索引，从０开始
									itemObj.visible = col.visible= true;　//是否显示
									itemObj.data = itemObj.name = col.name=fields[i];
									itemObj.width = col.width= currentColumns[k].sWidth
									for (var b =0;b < summaryCols.length; b++){
										if (currentColumns[k].data == summaryCols[b]){
											itemObj.isPagesum = col.isPagesum = true;
										}
									}
									newColumns.push(col);
									paramCols.push(itemObj);
									break
								}
							}
						}
						params.columns = paramCols;
						dataInfo.columns = newColumns;
						var exportInfoObj = dt.buttons.exportInfo( config );
						params.filename = dataInfo.fileName = exportInfoObj.filename.split('.')[0];
						params.title = dataInfo.title = exportInfoObj.title;
						//参数that.processing为启动或者关闭加载动画方法，that.processing（true）代表开启动画；that.processing(false)代表关闭动
						config.exportOptions.customExportAll(params,that.processing,dataInfo);
						$(".dt-button-collection").hide();
						return
					}
					var isSupportMap = typeof Map === 'function';//判断浏览器是否支持Map方法

					var renderFn = isSupportMap? new Map():[];
					var aoColumns = dt.settings()[0].aoColumns;
					for(var i=0;i<aoColumns.length;i++){
						if(aoColumns[i].data && aoColumns[i].render){
							var ofn = {fn:aoColumns[i].render,index:i};
							if(isSupportMap){
								renderFn.set(aoColumns[i].data,ofn);
							}else{
								renderFn.push({
									key:aoColumns[i].data,
									value:ofn
								})
							}
						}
					}
					var getRenderFnValue = function(key){ // 获取匹配key的对象
						for(var i = 0;i < renderFn.length ; i++){
							if(renderFn[i].key === key ){
								return renderFn[i].value
							}
						}
					}

					d.start = 0;
					var bodyData = [];
					$.ajax( {
						url: dt.ajax.url(),
						type: 'post',
						async: false,
						data: JSON.stringify(d),
						contentType: 'application/json',
						success: function (result) {
							var _exportTextarea = $('<textarea/>')[0];
							var strip = function ( str ) {
								if ( typeof str !== 'string' ) {
									return str;
								}
								str = str.replace( /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi, '' );

								var isMoney = str.indexOf('moneyType') > 0;//判断是否为moneyType输入组件  liuyj 2018/4/4
								var isInput = str.indexOf('input') > 0; //是否是input框
								if ( config.stripHtml && !isMoney && !isInput) {
									str = str.replace( /<[^>]*>/g, '' );
								}else{
									if(str){ //如果是moneyType输入组件,则对其值进行格式化  liuyj
										try {
											var elem = $(str);
											//如果标签是input 且不隐藏时，才判断此内容为input框；如果input为隐藏或者非input时，直接截取text
											if(elem[0].tagName =='INPUT' && elem[0].type !="hidden"){
												str = elem.val();
												if (isMoney){ //如果是moneyType控制
													var decial = elem.data('decimal') === undefined ? 2:elem.data('decimal');
													str =bt.money.format(str,{separator: ",",decimal: decial});
												}
											} else {
												str = str.replace( /<[^>]*>/g, '' );
											}
										}catch (e) {
											str = str.replace( /<[^>]*>/g, '' );
										}
									}
								}

								str = str.replace( /<[^>]*>/g, '' );//stripHtml
								str = str.replace( /^\s+|\s+$/g, '' );//trim
								str = str.replace( /\n/g, ' ' );//stripNewlines
								_exportTextarea.innerHTML = str;//decodeEntities
								str = _exportTextarea.value;
								// 处理金额去格式化
								return moneyUnformat &&moneyUnformat(str);
							};
							var list = [];
							if(result){
								try {
									if (typeof result == 'object') {
										list = result.data;
									} else {
										var temp = JSON.parse(result);
										list = temp.data;
									}
								}catch (e){
									list = [];
								}
							}
							if(list && list.length >0){
								for(var i=0;i<list.length;i++){
									var obj = [];
									for(var j=0;j<data.header.length;j++){
										var c = data.fields[j];
										if(isSupportMap){
											var rFn = renderFn.get(c);
										}else{
											var rFn = getRenderFnValue(c);
										}
										var val = list[i][c];
										if(rFn && rFn.fn){
											var meta = {row:i,col:rFn.index};
											val = rFn.fn(val,"",list[i],meta);
										}
										obj.push(strip(val));
									}
									bodyData.push(obj);
								}
								data.body = bodyData;
							}else { // list 为null时，代表后台报错
								data.body = [];
							}
						},
						error:function (e,e1,e2) {

						}
					});
				}

				if (data.body && !data.body.length){ //无数据时，直接弹框提示
					layer.alert(bt.lang('common.msg.temporarily_no_data'), {icon: 7});
					that.processing(false)
					return
				}

				var currentRow, rowNode;

				var addRow = function ( row,dt,aglin) {
					currentRow = rowPos+1;
					rowNode = _createNode( rels, "row", { attr: {r:currentRow} } );

					for ( var i=0, ien=row.length ; i<ien ; i++ ) {
						// Concat both the Cell Columns as a letter and the Row of the cell.
						var cellId = createCellPos(i) + '' + currentRow;
						var cell = null;

						// For null, undefined of blank cell, continue so it doesn't create the _createNode
						if ( row[i] === null || row[i] === undefined || row[i] === '' ) {
							continue;
						}
						row[i] = $.trim( row[i] );
						var aglinRef ;
						if(aglin){
							if(aglin[i] == 'center'){
								aglinRef = '51';//居中对齐
							}else if( aglin[i] == 'right'){
								aglinRef = '52';//居右对齐
							}else{
								aglinRef = '50';//居左对齐
							}
						}

						// Special number formatting options
						for ( var j=0, jen=_excelSpecials.length ; j<jen ; j++ ) {
							var special = _excelSpecials[j];

							// TODO Need to provide the ability for the specials to say
							// if they are returning a string, since at the moment it is
							// assumed to be a number
							//	if ( row[i].match && ! row[i].match(/^0\d+/) && row[i].match( special.match ) ) {
							//		var arr = row[i].toString().split(".");
							//		if( row[i].indexOf(',') === - 1 && arr[0] && arr[0].length > 3){ //无千分位及必须整数部分大于1000
							//			if(arr[0].length > 10){
							//				cell = _createNode( rels, 'c', {
							//					attr: {
							//						t: 'inlineStr',
							//						r: cellId,
							//						s:aglinRef || '50'
							//					},
							//					children:{
							//						row: _createNode( rels, 'is', {
							//							children: {
							//								row: _createNode( rels, 't', {
							//									text: row[i]
							//								} )
							//							}
							//						} )
							//					}
							//				} );
							//				break
							//			}else{
							//				//if(aglinRef != '51'){
							//				//	aglinRef = special.style ?special.style:aglinRef
							//				//}
							//				cell = _createNode( rels, 'c', {
							//					attr: {
							//						r: cellId,
							//						//s: special.style
							//						//s: special.style ?special.style:aglinRef
							//						s: aglinRef || 50
							//					},
							//					children: [
							//						_createNode( rels, 'v', { text: row[i] } )
							//					]
							//				} );
							//				break
							//			}
							//		}
							//		var val = row[i].replace(/[^\d\.\-]/g, '');
							//		if ( special.fmt ) {
							//			val = special.fmt( val );
							//		}
							//		//if(aglinRef != '51'){
							//		//	aglinRef = special.style ?special.style:aglinRef
							//		//}
							//		cell = _createNode( rels, 'c', {
							//			attr: {
							//				r: cellId,
							//				//s: special.style
							//				//s: special.style ?special.style:aglinRef
							//				s:aglinRef || 50
							//			},
							//			children: [
							//				_createNode( rels, 'v', { text: val } )
							//			]
							//		} );
							//		break;
							//	}
							//}
							cell = _createNode( rels, 'c', {
								attr: {
									t: 'inlineStr',
									r: cellId,
									s:aglinRef || '50'
								},
								children:{
									row: _createNode( rels, 'is', {
										children: {
											row: _createNode( rels, 't', {
												text: row[i]
											} )
										}
									} )
								}
							} );
							// 过虑时间有毫秒数的数据  "2023-10-19 18:40:14.000"
							if(row[i] && row[i].indexOf(":") === -1 && row[i].indexOf('%') === -1 && row[i].indexOf('‰') === -1){
								// 时间去格式化
								var arr = row[i].toString().split(".");
								//	if(arr.length < 3 &&arr[1] && parseFloat(arr[1]) >= 0){
								// 过滤掉 111测试7.23  111测试7.23测试
								if (arr.length < 3 && (arr[0] - 0) >= 0 && arr[1] && (arr[1] - 0) >= 0) {
									// 当小数位数超过2位时，使用 inlineStr 保持完整精度，避免 style 64 导致 Excel 只显示两位小数
									if (arr[1].length > 2) {
										cell = _createNode(rels, 'c', {
											attr: {
												t: 'inlineStr',
												r: cellId,
												s: aglinRef || '50'
											},
											children: {
												row: _createNode(rels, 'is', {
													children: {
														row: _createNode(rels, 't', {
															text: row[i]
														})
													}
												})
											}
										});
									} else {
										cell = _createNode(rels, 'c', {
											attr: {
												t: 'n',
												r: cellId,
												s: '64' // 金额格式化
											},
											children: [
												_createNode(rels, 'v', {text: row[i]})
											]
										});
									}
								}
								break
							}

							if ( row[i].match && ! row[i].match(/^0\d+/) && row[i].match( special.match ) ) {
								var arr = row[i].toString().split(".");
								if((row[i].indexOf(',') === - 1 && arr[0].indexOf('-') === -1 && arr[0] && arr[0].length > 3 )|| //无千分位及必须整数部分大于1000，非数字
									( arr[1] && arr[1].length > 2 && arr[1].charAt(arr[1].length -1) == 0)){ //三位有效数字且未尾数字为0时，直接按照字符串处理
									cell = _createNode( rels, 'c', {
										attr: {
											t: 'inlineStr',
											r: cellId,
											s:aglinRef || '50'
										},
										children:{
											row: _createNode( rels, 'is', {
												children: {
													row: _createNode( rels, 't', {
														text: row[i]
													} )
												}
											} )
										}
									} );
									break
								}
								var val = row[i].replace(/[^\d\.\-]/g, '');
								if ( special.fmt ) {
									val = special.fmt( val );
								}
								if (row[i].match && row[i].match(/^-?\d+(\.\d+)?$/) &&! row[i].match(/^0\d+/)&& arr[1] && arr[1].length > 2){ //
									cell = _createNode( rels, 'c', {
										attr:{
											t: 'n',
											r: cellId,
											s:aglinRef || '50'
										},
										children: [
											_createNode( rels, 'v', { text: val } )
										]
									} );
								} else {
									cell = _createNode( rels, 'c', {
										attr: {
											r: cellId,
											//s: special.style
											s: special.style ?special.style:aglinRef
											// s: aglinRef?aglinRef:special.style
										},
										children: [
											_createNode( rels, 'v', { text: val } )
										]
									} );
								}

								break;
							}
						}

						if ( ! cell ) {
							if ( typeof row[i] === 'number' || (
								row[i].match &&
								row[i].match(/^-?\d+(\.\d+)?$/) &&
								! row[i].match(/^0\d+/) )
							) {
								// Detect numbers - don't match numbers with leading zeros
								// or a negative anywhere but the start
								cell = _createNode( rels, 'c', {
									attr:{
										t: 'n',
										r: cellId
									},
									children: [
										_createNode( rels, 'v', { text: row[i] } )
									]
								} );
							}
							else {
								// String output - replace non standard characters for text output
								var text = ! row[i].replace ?
									row[i] :
									row[i].replace(/[\x00-\x09\x0B\x0C\x0E-\x1F\x7F-\x9F]/g, '');

								if( text.lastIndexOf('%') === text.length -1 || text == '--'){
									cell = _createNode( rels, 'c', { //　百分比或“－－”无数据的都靠右显示
										attr: {
											t: 'inlineStr',
											r: cellId,
											s:aglinRef || '50'
										},
										children:{
											row: _createNode( rels, 'is', {
												children: {
													row: _createNode( rels, 't', {
														text: text
													} )
												}
											} )
										}
									} );
								}else{
									cell = _createNode( rels, 'c', {
										attr: {
											t: 'inlineStr',
											r: cellId,
										},
										children:{
											row: _createNode( rels, 'is', {
												children: {
													row: _createNode( rels, 't', {
														text: text
													} )
												}
											} )
										}
									} );
								}
							}
						}
						rowNode.appendChild( cell );
					}
					relsGet.appendChild(rowNode);
					rowPos++;
				};

				//var addRow = function ( row,dt,aglin) {
				//	currentRow = rowPos+1;
				//	rowNode = _createNode( rels, "row", { attr: {r:currentRow} } );
				//
				//	for ( var i=0, ien=row.length ; i<ien ; i++ ) {
				//		// Concat both the Cell Columns as a letter and the Row of the cell.
				//		var cellId = createCellPos(i) + '' + currentRow;
				//		var cell = null;
				//
				//		// For null, undefined of blank cell, continue so it doesn't create the _createNode
				//		if ( row[i] === null || row[i] === undefined || row[i] === '' ) {
				//			continue;
				//		}
				//
				//		row[i] = $.trim( row[i] );
				//		var aglinRef;//设置内容对齐方式
				//		if(aglin){
				//			if(aglin[i] == 'center'){
				//				aglinRef = '51';//居中对齐
				//			}else if( aglin[i] == 'right'){
				//				aglinRef = '52';//居右对齐
				//			}else{
				//				aglinRef = '50' //居左对齐
				//			}
				//		}
				//
				//       var attrObj = {
				//           t: 'inlineStr',
				//           r: cellId
				//       };
				//       if(aglinRef){
				//           attrObj.s = aglinRef;
				//       }
				//
				//		// Special number formatting options
				//		for ( var j=0, jen=_excelSpecials.length ; j<jen ; j++ ) {
				//			var special = _excelSpecials[j];
				//
				//			// TODO Need to provide the ability for the specials to say
				//			// if they are returning a string, since at the moment it is
				//			// assumed to be a number
				//			if ( row[i].match && ! row[i].match(/^0\d+/) && row[i].match( special.match )) {
				//				var val = row[i];
				//				//var val = row[i].replace(/[^\d\.\-]/g, '');
				//
				//				if ( special.fmt ) {
				//					val = special.fmt( val );
				//                  // valStr = special.fmt( val );
				//				}
				//
				//				//TODO:zhoujian 防止数字太长后面被截取掉
				//				//if(val && val.length>10){
				//                //   cell = _createNode( rels, 'c', {
				//                //       attr:attrObj,
				//                //       children:{
				//                //           row: _createNode( rels, 'is', {
				//                //               children: {
				//                //                   row: _createNode( rels, 't', {
				//                //                       // text: val
				//				//						//text:valStr //超过10位时，直接显示对应的值
				//				//						text:val.toString() //超过10位时，直接显示对应的值
				//                //                   } )
				//                //               }
				//                //           } )
				//                //       }
				//                //   } );
				//				//}else{
				//					//小数点大于位
				//					var arr = val.toString().split(".");
				//					var isZero = arr[1] && arr[1].charAt( arr[1].length-1) == 0;
				//					if( val && isZero ){
				//                       cell = _createNode( rels, 'c', {
				//                           attr:attrObj,
				//                           children:{
				//                               row: _createNode( rels, 'is', {
				//                                   children: {
				//                                       row: _createNode( rels, 't', {
				//                                           text: val
				//                                       } )
				//                                   }
				//                               } )
				//                           }
				//                       } );
				//					}else{
				//                       cell = _createNode( rels, 'c', {
				//							attr: {
				//								r: cellId,
				//								s: special.style
				//								//t:'n',
				//								//s: aglinRef
				//							},
				//                           children: [
				//                               _createNode( rels, 'v', { text: val } )
				//                           ]
				//                       } );
				//					}
				//				//}
				//				/*cell = _createNode( rels, 'c', {
				//					attr: {
				//						r: cellId,
				//						s: special.style
				//					},
				//					children: [
				//						_createNode( rels, 'v', { text: val } )
				//					]
				//				} );*/
				//				break;
				//			}
				//		}
				//
				//		if ( ! cell ) {
				//			if ( typeof row[i] === 'number' || (
				//				row[i].match &&
				//				row[i].match(/^-?\d+(\.\d+)?$/) &&
				//				! row[i].match(/^0\d+/)  )
				//			) {
				//				// Detect numbers - don't match numbers with leading zeros
				//				// or a negative anywhere but the start
				//				cell = _createNode( rels, 'c', {
				//					attr: {
				//						t: 'n',
				//						r: cellId,
				//					},
				//					children: [
				//						_createNode( rels, 'v', { text: row[i] } )
				//					]
				//				} );
				//			}
				//			else {
				//				// String output - replace non standard characters for text output
				//				var text = ! row[i].replace ?
				//					row[i] :
				//					row[i].replace(/[\x00-\x09\x0B\x0C\x0E-\x1F\x7F-\x9F]/g, '');
				//
				//				if((/^0\d+/).test(row[i])){ //判断是否为数字字符串 01 ,02
				//                   cell = _createNode( rels, 'c', {
				//                       attr:attrObj,
				//                       children:{
				//                           row: _createNode( rels, 'is', {
				//                               children: {
				//                                   row: _createNode( rels, 't', {
				//                                       text: text
				//                                   } )
				//                               }
				//                           } )
				//                       }
				//                   } );
				//				}else{
				//					cell = _createNode( rels, 'c', {
				//						attr:attrObj,
				//						children:{
				//							row: _createNode( rels, 'is', {
				//								children: {
				//									row: _createNode( rels, 't', {
				//										text: text
				//									} )
				//								}
				//							} )
				//						}
				//					} );
				//
				//				}
				//			}
				//		}
				//		rowNode.appendChild( cell );
				//	}
				//	relsGet.appendChild(rowNode);
				//	rowPos++;
				//};

				$( 'sheets sheet', xlsx.xl['workbook.xml'] ).attr( 'name', _sheetname( config ) );
				if ( config.customizeData ) {
					config.customizeData( data );
				}

				var mergeCells = function ( row, colspan ,start) {
					var mergeCells = $('mergeCells', rels);
					var x;
					if(!start){
						x =  'A'+row;
					}else{
						x = createCellPos(start) + row;
					}

					mergeCells[0].appendChild( _createNode( rels, 'mergeCell', {
						attr: {
							// ref: 'A'+start+':'+createCellPos(colspan)+row
							ref: x+':'+createCellPos(colspan)+row
						}
					} ) );
					$('row:eq('+(row-1)+') c', rels).attr( 's', '51' );// centre
					mergeCells.attr( 'count', mergeCells.attr( 'count' )+1 );

				};

				// 针对双表头的合并方法
				var mergeRow =  function ( rowPos,rowspanEnd ,rowspanStart) {
					var mergeCells = $('mergeCells', rels);
					var x = createCellPos(rowspanStart) + ( rowPos -1) ;
					mergeCells[0].appendChild( _createNode( rels, 'mergeCell', {
						attr: {
							ref: x+':'+createCellPos(rowspanStart)+ rowspanEnd
						}
					} ) );
					mergeCells[0].appendChild( _createNode( rels, 'mergeCell', {
						attr: {
							ref:'H1:J1'
						}
					} ) );
					$('row:eq('+(rowPos-1)+') c', rels).attr( 's', '51' );
					mergeCells.attr( 'count', mergeCells.attr( 'count' )+1 );
				}

				// Title and top messages
				var exportInfo = dt.buttons.exportInfo( config );
				if ( exportInfo.title ) {
					addRow( [exportInfo.title], rowPos );
					mergeCells( rowPos, data.header.length-1 );
					$('row:eq('+(rowPos-1)+') c', rels).attr( 's', '67' );// 加粗及居中
				}
				if ( exportInfo.messageTop ) {
					addRow( [exportInfo.messageTop], rowPos );
					mergeCells( rowPos, data.header.length-1 );
				}

				// 导出excel扩展单行信息 created by chenlw on 2020-12-18
				var extendExcelSingleRowInfoCallback = config.extendExcelSingleRowInfoCallback;
				if ( extendExcelSingleRowInfoCallback ) {
					var extendExcelSingleRowInfo = extendExcelSingleRowInfoCallback();
					if(extendExcelSingleRowInfo && extendExcelSingleRowInfo.length > 0){
						addRow( extendExcelSingleRowInfo, rowPos );
					}
				}

				if ( config.header ) {
					var headerTrs = $(dt.table().header()).find('tr');
					var length = headerTrs.length;
					if( length == 1){ //单表头
						addRow( data.header, rowPos );
						// $('row:last c', rels).attr( 's', '2' ); // bold
						$('row:last c', rels).attr( 's', '67' )
					}else{//多表头
						/**
						 *  1. 数据要求{ text:表头内容,rowSpan：跨行数,colSpan：跨行数}
						 *  2. 如果被跨行或者跨列，都有数据点位，只是rowSpan/colSpan 无值，text:''
						 *  3. 添加行之后，统一根据rowspan / colspan来设置全并行或全并列等
						 * */
							// 列常量 列常量,支持26*3列数
						var excelCellNumber = ['start']
						var zimu = ['','A','B','C'];
						var englishZiMu = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
						for (var i = 0; i < zimu.length; i++) {
							for (var j = 0; j < englishZiMu.length; j++) {
								excelCellNumber.push(zimu[i]+englishZiMu[j]);
							}
						}
						// 1. 按照行进行遍历所有th
						var tableMultiData = [];
						var preRowSpanRowList = []; // 前一行跨行信息，跨行将会引影下一行的列索引
						for(var i = 0; i< length;i++){
							var currentRowData = [];
							// 如果第一行有数据，则创建一个空的数组，长度与第一个样的
							if(tableMultiData[0]){
								var emptyTableThsArr = new Array(tableMultiData[0].length).fill("");
								// 如果有跨行信息，将对emptyTableThsArr 进行点位处理
								if(preRowSpanRowList && preRowSpanRowList.length){
									for (var b = 0; b < preRowSpanRowList.length; b++) {
										emptyTableThsArr[preRowSpanRowList[b].colIndex] = {
											text:''
										}
										if(preRowSpanRowList[b].rowSpan > 1){
											// 如果有跨行及跨列，需要处理跨列的相关信息
											if(preRowSpanRowList[b].colSpan > 1){
												var colStart = preRowSpanRowList[b].colIndex;
												for (var j = 1; j < preRowSpanRowList[b].colSpan; j++) {
													colStart = colStart + 1
													emptyTableThsArr[colStart] = {
														text:''
													}
												}

											}
											preRowSpanRowList[b].rowSpan--;
										}
										if(preRowSpanRowList[b].rowSpan < 2){
											// 如果小于等于2，直接删除
											preRowSpanRowList.splice(b, 1)
											b--;
										}

									}
								}
								currentRowData = emptyTableThsArr
							}

							// 1.1 编辑当前行所有th
							var currentRowAllThs = $(headerTrs[i]).find('th');
							var colIndex = 0;
							for (var j = 0; j < currentRowAllThs.length; j++) {
								var cell = $(currentRowAllThs[j]);
								// 如果是操作列或复选框列，则跳过
								if(cell.hasClass('extraCol')){
									continue
								}
								var cellInfo = {
									text:cell.text()
								}
								var rowSpan = parseInt(cell.attr('rowspan')) || 1;
								var colSpan = parseInt(cell.attr('colspan')) || 1;
								if(rowSpan > 1){
									cellInfo.rowSpan = rowSpan;
									// 暂时只考虑跨两行 currentRowData.length就是列索引，从0开始
									var currentThInfo = {rowSpan:rowSpan,colIndex:currentRowData.length};
									if(colSpan > 1){ // 如果有跨行也有跨行，都要标记,影响到了下一行的th位置
										currentThInfo.colSpan = colSpan;
									}
									preRowSpanRowList.push(currentThInfo);
								}

								if(colSpan > 1){
									cellInfo.colSpan = colSpan;
									// 第一行时，直接追加
									if(!tableMultiData[0]){
										currentRowData.push(cellInfo);
									}else{
										// 如果没有值，就直接添加
										colIndex = createMulitHeaderList(colIndex,currentRowData,cellInfo);
									}

									// 用空字符串占位对应的列数
									for (var k = 1; k < colSpan; k++) {
										// 第一行
										if(!tableMultiData[0]){
											currentRowData.push({
												text:''
											})
										}else{
											colIndex++
											// 判定是否已经被点位，如果被占了，则找下一个列
											colIndex = createMulitHeaderList(colIndex,currentRowData, {text:''});
										}
									}
								}else{
									// 第一行时，直接追加
									if(!tableMultiData[0]){
										// 无合并列的
										currentRowData.push(cellInfo);
									}else{
										// colIndex ++;
										// 如果没有值，就直接添加
										colIndex = createMulitHeaderList(colIndex,currentRowData,cellInfo);
									}
								}
							}
							tableMultiData.push(currentRowData)
						}
						// 2. 添加行及设置行列合并信息
						var tableHeader = tableMultiData;
						if(tableHeader && tableHeader.length){
							for (var i = 0; i < tableHeader.length; i++) {
								// 1. 获取当前表格内容
								var headerText = [];
								for (var j = 0; j < tableHeader[i].length; j++){
									headerText.push(tableHeader[i][j].text);
								}
								// 2. 新增一行内容
								addRow( headerText, rowPos );

								// 3. 获取合并行列信息
								var mergeCells = $('mergeCells', rels);
								for (var j = 0; j < tableHeader[i].length; j++){
									var tableHeaderList = tableHeader[i][j]
									// 处理跨行跨列信息
									var currentColIndex = j+1;
									var ref = '';
									//  跨列信息
									if(tableHeaderList.colSpan && tableHeaderList.colSpan > 1){
										var spanColIndex = currentColIndex + tableHeaderList.colSpan - 1;
										// A1:C1
										ref =  excelCellNumber[currentColIndex]+rowPos+':' + excelCellNumber[spanColIndex]+rowPos
									}
									//  跨行信息
									if(tableHeaderList.rowSpan && tableHeaderList.rowSpan > 1){
										var spanRowIndex = rowPos + tableHeaderList.rowSpan - 1;
										// A1:A2
										if(ref){
											ref = excelCellNumber[currentColIndex]+rowPos+':' + excelCellNumber[spanColIndex] + spanRowIndex
										}else{
											ref = excelCellNumber[currentColIndex]+rowPos+':' + excelCellNumber[currentColIndex]+spanRowIndex
										}
									}

									if(ref){
										mergeCells[0].appendChild( _createNode( rels, 'mergeCell', {
											attr: {
												// ref: 'A'+start+':'+createCellPos(colspan)+row
												ref:ref
											}
										} ) );
									}

								}
								// 4. 设置样式
								// $('row:eq('+(rowPos-1)+') c', rels).attr( 's', '51' ) // 居中
								// $('row:last c', rels).attr( 's', '2' ); // bold

								$('row:eq('+(rowPos-1)+') c', rels).attr( 's', '67' ) // 加粗居中
							}
						}
					}
				}

				// 递归生成多表头数据 l 为列索引 list为表头数据 obj为当前列表头信
				function createMulitHeaderList(l,list,obj) {
					if(!list[l]){
						list[l] = obj;
					}else{
						l++;
						return createMulitHeaderList(l,list,obj)
					};
					return l
				}
				// Table itself
				// if ( config.header ) {
				// 	addRow( data.header, rowPos);
				// 	$('row:last c', rels).attr( 's', '2' ); // bold
				// }
				for ( var n=0, ie=data.body.length ; n<ie ; n++ ) {
					addRow( data.body[n],rowPos,data.align);//新增加参数align，用于设置表格对齐方式 liuyj 20180425
				}
				/*
                * 汇总信息要添加了导入内容中
                *  liuyj 2018-04-16
                *  未考虑本位币之前的转换
                */
				var rowElem = dt.rows().nodes();
				var summaryELem = $(rowElem).siblings('.btsummary');

				if(summaryELem.length && config.exportOptions.type !=="all" && config.exportOptions.type !== 'selectedExport'){
					for(var i = 0; i < summaryELem.length; i++){
						var dataArr = [];
						var tds = $(summaryELem[i]).find('td:not(.extraCol):visible');
						for(var j = 0;j< tds.length;j++){
							var text = $(tds[j]).text();
							if( config.exportOptions.type==="all" && text) {//导出1000数据
								if( tds[j].className && (tds[j].className.indexOf('allSum')> -1 || tds[j].className.indexOf('pageSum')> -1) ){
									var allSum = 0;
									var suffix = $(tds[j]).data('suffix');
									var money = $(tds[j]).data('money');
									if(suffix){
										text = text.replace(suffix,'');
									}

									if( $(tds[j]).data('sum-callback') && data.sumCallback){ //新增加自定义汇总方法回调
										var sumObj = {};
										var sumtd = $(tds[j]).siblings('.sumtd');
										$.each(sumtd,function(){
											var name = $(this).data('sum-name');
											sumObj[name] = $(this).text();
										})
										text = data.sumCallback[$(tds[j]).data('sum-name')]['expression'](sumObj);
									}else{
										var decimal = text.split('.')[1] && text.split('.')[1].length;
										var sumPageText = $(tds[j]).data('sumpage-text');//获取分页汇总的字段
										for( var k = 0;k < data.body.length;k++){
											var currentQty = '0';
											if(!sumPageText){
												currentQty = data.body[k][j] ? data.body[k][j].replace(new RegExp( "," , "g" ),''):'0';
											}else{
												if( data.body[k].indexOf(sumPageText) > -1 ){ //判断该分页字段是否包含，如果是，则进行取值
													currentQty = data.body[k][j] ? data.body[k][j].replace(new RegExp( "," , "g" ),''):'0';
												}
											}
											//var currentQty = data.body[k][j] ? data.body[k][j].replace(new RegExp( "," , "g" ),''):0;
											if(suffix){
												currentQty = currentQty.replace(suffix,'');
											}
											if( $.isNumeric(currentQty) ){ //其它非数字字符不处理
												allSum += ( currentQty - 0);
											}
										}
										if(suffix =='%'){ //百分比不能大于100%
											text = allSum > 100 ? 100:allSum;
										}
										if( money != false ){
											text = bt.money.format(allSum,{
												decimal:decimal
											});
										}else{
											text = allSum;
										}
										text = text + suffix;//添加后缀标识，正常情况为‘’;
									}
								}
							}
							// 处理金额去格式化
							text = moneyUnformat &&moneyUnformat(text);

							if(!text){
								text = '';
							}
							dataArr.push(text);
						}
						addRow( dataArr, rowPos,data.align);
					}
				}else if(config.exportOptions.type ==="all"){ //1.导出所有;
					//转number
					function intVal(i) {
						if (typeof i === 'string') { //当数字被格式化时
							i = i.replace(/,/g, '')
						}
						if ($.isNumeric(i)) { //其它非数字字符不处理
							return i - 0;
						} else {
							return 0;
						}
					}
					var summaryConfig = $(dt.tables().nodes()[0]).data("summary"); //获取存贮的汇总参数信息
					if (!summaryConfig || summaryConfig.position || summaryConfig.convertMoney || summaryConfig.natualCurrency || summaryConfig.outerData){
						//导出所有，暂不支持自定义标题、自定义位置、金额转换、本位币计算、引入外面数据
					}else if(config.exportOptions.type !== 'selectedExport'){
						var moneyDecial = {
							decimal:(summaryConfig.format && summaryConfig.format.decimal)? summaryConfig.format.decimal:2
						}
						if (summaryConfig){
							var sumList = summaryConfig.sumList;
							var summaryCommonText = { //汇总公共字段
								selectSum:bt.lang('table.datatable.subtotalSelected'),//已选小计
								pageSum:bt.lang('table.adjustDetail.moneySum')//合计
							}
							var arr = [];
							for (var i = 0,length = data.fields.length; i < length; i++){
								arr.push("");
							}
							var bodyCurrentField = {};
							if (summaryConfig.groupColumn !== undefined){ //分类汇总合计
								var dataIndex = data.fields.indexOf(summaryConfig.groupColumn);//获取该分类字段数据中的索引

								if (dataIndex !== -1 && sumList !== undefined && data.body.length){
									/**
									 * bodyCurrentField
									 * body数据中当前分类字段下的内容,要去重
									 * 数据格式为：{ "人民币":{"借款余额":[1222,36666]}}
									 * */
									for (var i = 0,length = data.body.length; i < length; i++){
										var groupText = data.body[i][dataIndex]
										if (bodyCurrentField[groupText] === undefined){
											bodyCurrentField[groupText] = {}
										}
										for (var j = 0,length1 = sumList.length; j < length1; j++){
											var summaryColIndex = data.fields.indexOf(sumList[j].dataName);
											if (bodyCurrentField[groupText][sumList[j].dataName] === undefined){
												// bodyCurrentField[groupText][sumList[j].dataName] = []
												bodyCurrentField[groupText][sumList[j].dataName] = {
													list:[],
													expression:sumList[j].expression
												}
											}
											bodyCurrentField[groupText][sumList[j].dataName].list.push(data.body[i][summaryColIndex]);
										}
									}

									for (var key in bodyCurrentField){
										var currentArr = [].concat(arr);//复制一个数组
										if (summaryConfig.pageSum){
											currentArr[0] = key + summaryCommonText["pageSum"];
										}
										for (var n in bodyCurrentField[key]){
											var index = data.fields.indexOf(n);
											if (index !== -1){
												var total = bodyCurrentField[key][n].list.reduce(function (a, b) {
													return intVal(a) + intVal(b);
												}, 0);

												// 判断汇总是否有自定义expression 回调，如果有，直接执行
												if(bodyCurrentField[key][n].expression && typeof  bodyCurrentField[key][n].expression){
													total = bodyCurrentField[key][n].expression(total,bodyCurrentField[key][n].list,key)
												}

												// currentArr[index] = bt.money.format(total,moneyDecial)
												// 处理金额去格式化
												if(moneyUnformat){
													currentArr[index] = moneyUnformat &&moneyUnformat(bt.money.format(total,moneyDecial));
												}else{
													currentArr[index] = bt.money.format(total,moneyDecial)
												}
											}
										}
										addRow( currentArr, rowPos,data.align)
									}
								}
							}else if (summaryConfig.pageSum){ //合计
								if (sumList !== undefined){
									/**
									 * bodyCurrentField
									 * body数据中当前分类字段下的内容,要去重
									 * 数据格式为：{"借款余额":[1222,36666]}
									 * */
									for (var i = 0,length = data.body.length; i < length; i++){
										for (var j = 0,length1 = sumList.length; j < length1; j++){
											var summaryColIndex = data.fields.indexOf(sumList[j].dataName);
											if (bodyCurrentField[sumList[j].dataName] === undefined){
												bodyCurrentField[sumList[j].dataName] = []
											}
											bodyCurrentField[sumList[j].dataName].push(data.body[i][summaryColIndex]);
										}
									}
									var currentArr = [].concat(arr);//复制一个数组
									currentArr[0] = summaryCommonText["pageSum"];
									for (var n in bodyCurrentField){
										var index = data.fields.indexOf(n);
										if (index !== -1){
											var total = bodyCurrentField[n].reduce(function (a, b) {
												return intVal(a) + intVal(b);
											}, 0);
											// currentArr[index] = bt.money.format(total,moneyDecial)
											// 处理金额去格式化
											// 处理金额去格式化
											if(moneyUnformat){
												currentArr[index] = moneyUnformat &&moneyUnformat(bt.money.format(total,moneyDecial));
											}else{
												currentArr[index] = bt.money.format(total,moneyDecial)
											}
										}
									}
									addRow( currentArr, rowPos,data.align)
								}
							}
						}
					}
				}

				if ( config.footer && data.footer ) {
					addRow( data.footer, rowPos);
					$('row:last c', rels).attr( 's', '2' ); // bold

				}

				// Below the table
				if ( exportInfo.messageBottom ) {
					addRow( [exportInfo.messageBottom], rowPos );
					mergeCells( rowPos, data.header.length-1 );
				}

				// Set column widths
				var cols = _createNode( rels, 'cols' );
				$('worksheet', rels).prepend( cols );

				for ( var i=0, ien=data.header.length ; i<ien ; i++ ) {
					cols.appendChild( _createNode( rels, 'col', {
						attr: {
							min: i+1,
							max: i+1,
							width: _excelColWidth( data, i ),
							customWidth: 1
						}
					} ) );
				}

				// Let the developer customise the document if they want to
				if ( config.customize ) {
					config.customize( xlsx );
				}

				var jszip = _jsZip();
				var zip = new jszip();
				var zipConfig = {
					type: 'blob',
					mimeType: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
				};

				_addToZip( zip, xlsx );
				if ( zip.generateAsync ) {
					// JSZip 3+
					zip
						.generateAsync( zipConfig )
						.then( function ( blob ) {
							_saveAs( blob, exportInfo.filename );
							that.processing( false );
						} );
				}
				else {
					// JSZip 2.5
					_saveAs(
						zip.generate( zipConfig ),
						exportInfo.filename
					);
					this.processing( false );
				}
			},

			filename: '*',

			extension: '.xlsx',

			exportOptions: {},

			header: true,

			footer: false,

			title: '*',

			messageTop: '*',

			messageBottom: '*'
		};

//
// PDF export - using pdfMake - http://pdfmake.org
//
		DataTable.ext.buttons.pdfHtml5 = {
			className: 'buttons-pdf buttons-html5',

			available: function () {
				return window.FileReader !== undefined && _pdfMake();
			},

			text: function ( dt ) {
				return dt.i18n( 'buttons.pdf', 'PDF' );
			},

			action: function ( e, dt, button, config ) {
				this.processing( true );

				var that = this;
				var data = dt.buttons.exportData( config.exportOptions );
				var info = dt.buttons.exportInfo( config );
				var rows = [];

				if ( config.header ) {
					rows.push( $.map( data.header, function ( d ) {
						return {
							text: typeof d === 'string' ? d : d+'',
							style: 'tableHeader'
						};
					} ) );
				}

				for ( var i=0, ien=data.body.length ; i<ien ; i++ ) {
					rows.push( $.map( data.body[i], function ( d ) {
						return {
							text: typeof d === 'string' ? d : d+'',
							style: i % 2 ? 'tableBodyEven' : 'tableBodyOdd'
						};
					} ) );
				}

				if ( config.footer && data.footer) {
					rows.push( $.map( data.footer, function ( d ) {
						return {
							text: typeof d === 'string' ? d : d+'',
							style: 'tableFooter'
						};
					} ) );
				}

				var doc = {
					pageSize: config.pageSize,
					pageOrientation: config.orientation,
					content: [
						{
							table: {
								headerRows: 1,
								body: rows
							},
							layout: 'noBorders'
						}
					],
					styles: {
						tableHeader: {
							bold: true,
							fontSize: 11,
							color: 'white',
							fillColor: '#2d4154',
							alignment: 'center'
						},
						tableBodyEven: {},
						tableBodyOdd: {
							fillColor: '#f3f3f3'
						},
						tableFooter: {
							bold: true,
							fontSize: 11,
							color: 'white',
							fillColor: '#2d4154'
						},
						title: {
							alignment: 'center',
							fontSize: 15
						},
						message: {}
					},
					defaultStyle: {
						fontSize: 10
					}
				};

				if ( info.messageTop ) {
					doc.content.unshift( {
						text: info.messageTop,
						style: 'message',
						margin: [ 0, 0, 0, 12 ]
					} );
				}

				if ( info.messageBottom ) {
					doc.content.push( {
						text: info.messageBottom,
						style: 'message',
						margin: [ 0, 0, 0, 12 ]
					} );
				}

				if ( info.title ) {
					doc.content.unshift( {
						text: info.title,
						style: 'title',
						margin: [ 0, 0, 0, 12 ]
					} );
				}

				if ( config.customize ) {
					config.customize( doc, config );
				}

				var pdf = _pdfMake().createPdf( doc );

				if ( config.download === 'open' && ! _isDuffSafari() ) {
					pdf.open();
					this.processing( false );
				}
				else {
					pdf.getBuffer( function (buffer) {
						var blob = new Blob( [buffer], {type:'application/pdf'} );

						_saveAs( blob, info.filename );
						that.processing( false );
					} );
				}
			},

			title: '*',

			filename: '*',

			extension: '.pdf',

			exportOptions: {},

			orientation: 'portrait',

			pageSize: 'A4',

			header: true,

			footer: false,

			messageTop: '*',

			messageBottom: '*',

			customize: null,

			download: 'download'
		};


		return DataTable.Buttons;
	}));

	exports('html5')
})
