package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Popups;


import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * Class that filters the text in the form to be a double like format.
 */
public class DoubleFilter extends DocumentFilter {
    /**
     * Invoked prior to insertion of text into the
     * specified Document. Subclasses that want to conditionally allow
     * insertion should override this and only call supers implementation as
     * necessary, or call directly into the FilterBypass.
     *
     * @param fb     FilterBypass that can be used to mutate Document
     * @param offset the offset into the document to insert the content &gt;= 0.
     *               All positions that track change at or after the given location
     *               will move.
     * @param string the string to insert
     * @param attr   the attributes to associate with the inserted
     *               content.  This may be null if there are no attributes.
     * @throws BadLocationException the given insert position is not a
     *                              valid position within the document
     */
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (isDouble(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    /**
     * Invoked prior to replacing a region of text in the
     * specified Document. Subclasses that want to conditionally allow
     * replace should override this and only call supers implementation as
     * necessary, or call directly into the FilterBypass.
     *
     * @param fb     FilterBypass that can be used to mutate Document
     * @param offset Location in Document
     * @param length Length of text to delete
     * @param text   Text to insert, null indicates no text to insert
     * @param attrs  AttributeSet indicating attributes of inserted text,
     *               null is legal.
     * @throws BadLocationException the given insert position is not a
     *                              valid position within the document
     */
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if (isDouble(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    /**
     * Function that checks if the new text can be parsed as a Double.
     * @param s new String.
     * @return Boolean True if the text can be parsed, False otherwise
     */
    private boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
