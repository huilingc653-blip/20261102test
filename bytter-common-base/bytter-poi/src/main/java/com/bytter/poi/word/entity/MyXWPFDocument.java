package com.bytter.poi.word.entity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import java.io.InputStream;
/**
 * Created by zhouj on 2017/6/2.
 */
@Slf4j
public class MyXWPFDocument extends XWPFDocument {
    private static String PICXML = "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">         <pic:nvPicPr>            <pic:cNvPr id=\"%s\" name=\"Generated\"/>            <pic:cNvPicPr/>         </pic:nvPicPr>         <pic:blipFill>            <a:blip r:embed=\"%s\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>            <a:stretch>               <a:fillRect/>            </a:stretch>         </pic:blipFill>         <pic:spPr>            <a:xfrm>               <a:off x=\"0\" y=\"0\"/>               <a:ext cx=\"%s\" cy=\"%s\"/>            </a:xfrm>            <a:prstGeom prst=\"rect\">               <a:avLst/>            </a:prstGeom>         </pic:spPr>      </pic:pic>   </a:graphicData></a:graphic>";
    public MyXWPFDocument() {
    }
    public MyXWPFDocument(InputStream in) throws Exception {
        super(in);
    }
    public MyXWPFDocument(OPCPackage opcPackage) throws Exception {
        super(opcPackage);
    }
    public void createPicture(String blipId, int id, int width, int height) {
        boolean EMU = true;
        width *= 9525;
        height *= 9525;
        CTInline inline = this.createParagraph().createRun().getCTR().addNewDrawing().addNewInline();
        String picXml = String.format(PICXML,
                new Object[] { Integer.valueOf(id), blipId, Integer.valueOf(width), Integer.valueOf(height) });
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException var11) {
            log.error(var11.getMessage(), var11.fillInStackTrace());
        }
        inline.set(xmlToken);
        inline.setDistT(0L);
        inline.setDistB(0L);
        inline.setDistL(0L);
        inline.setDistR(0L);
        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx((long) width);
        extent.setCy((long) height);
        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId((long) id);
        docPr.setName("Picture " + id);
        docPr.setDescr("Generated");
    }
    public void createPicture(XWPFRun run, String blipId, int id, int width, int height) {
        boolean EMU = true;
        width *= 9525;
        height *= 9525;
        CTInline inline = run.getCTR().addNewDrawing().addNewInline();
        String picXml = String.format(PICXML,
                new Object[] { Integer.valueOf(id), blipId, Integer.valueOf(width), Integer.valueOf(height) });
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException var12) {
            log.error(var12.getMessage(), var12.fillInStackTrace());
        }
        inline.set(xmlToken);
        inline.setDistT(0L);
        inline.setDistB(0L);
        inline.setDistL(0L);
        inline.setDistR(0L);
        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx((long) width);
        extent.setCy((long) height);
        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId((long) id);
        docPr.setName("Picture " + id);
        docPr.setDescr("Generated");
    }
}
