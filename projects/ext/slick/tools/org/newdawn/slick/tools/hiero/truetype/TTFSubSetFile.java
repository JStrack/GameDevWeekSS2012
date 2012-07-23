/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id: TTFSubSetFile.java 426576 2006-07-28 15:44:37Z jeremias $ */
 
package org.newdawn.slick.tools.hiero.truetype;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Reads a TrueType file and generates a subset
 * that can be used to embed a TrueType CID font.
 * TrueType tables needed for embedded CID fonts are:
 * "head", "hhea", "loca", "maxp", "cvt ", "prep", "glyf", "hmtx" and "fpgm".
 * The TrueType spec can be found at the Microsoft
 * Typography site: http://www.microsoft.com/truetype/
 */
public class TTFSubSetFile extends TTFFile {
	/** The output to write into */
    private byte[] output = null;
    /** THe real size of the sub set */
    private int realSize = 0;
    /** The current position we'll write to or read from */
    private int currentPos = 0;

    /**
     * Offsets in name table to be filled out by table.
     * The offsets are to the checkSum field
     */
    private int cvtDirOffset = 0;
    /**
     * Offsets in name table to be filled out by table.
     * The offsets are to the checkSum field
     */
    private int fpgmDirOffset = 0;
    /**
     * Offsets in name table to be filled out by table.
     * The offsets are to the checkSum field
     */
    private int glyfDirOffset = 0;
    /**
     * Offsets in name table to be filled out by table.
     * The offsets are to the checkSum field
     */
    private int headDirOffset = 0;
    /**
     * Offsets in name table to be filled out by table.
     * The offsets are to the checkSum field
     */
    private int hheaDirOffset = 0;
    /**
     * Offsets in name table to be filled out by table.
     * The offsets are to the checkSum field
     */
    private int hmtxDirOffset = 0;
    /**
     * Offsets in name table to be filled out by table.
     * The offsets are to the checkSum field
     */
    private int locaDirOffset = 0;
    /**
     * Offsets in name table to be filled out by table.
     * The offsets are to the checkSum field
     */
    private int maxpDirOffset = 0;
    /**
     * Offsets in name table to be filled out by table.
     * The offsets are to the checkSum field
     */
    private int prepDirOffset = 0;

    /** The check sum adjustment */
    private int checkSumAdjustmentOffset = 0;
    /** The offset to the locas */
    private int locaOffset = 0;

    /**
     * Initalize the output array
     * 
     * @param size The size of the output array 
     */
    private void init(int size) {
        output = new byte[size];
        realSize = 0;
        currentPos = 0;

        // createDirectory()
    }

    /**
     * Create the directory table
     */
    private void createDirectory() {
        int numTables = 9;
        // Create the TrueType header
        writeByte((byte)0);
        writeByte((byte)1);
        writeByte((byte)0);
        writeByte((byte)0);
        realSize += 4;

        writeUShort(numTables);
        realSize += 2;

        // Create searchRange, entrySelector and rangeShift
        int maxPow = maxPow2(numTables);
        int searchRange = maxPow * 16;
        writeUShort(searchRange);
        realSize += 2;

        writeUShort(maxPow);
        realSize += 2;

        writeUShort((numTables * 16) - searchRange);
        realSize += 2;

        // Create space for the table entries
        writeString("cvt ");
        cvtDirOffset = currentPos;
        currentPos += 12;
        realSize += 16;

        if (hasFpgm()) {
            writeString("fpgm");
            fpgmDirOffset = currentPos;
            currentPos += 12;
            realSize += 16;
        }

        writeString("glyf");
        glyfDirOffset = currentPos;
        currentPos += 12;
        realSize += 16;

        writeString("head");
        headDirOffset = currentPos;
        currentPos += 12;
        realSize += 16;

        writeString("hhea");
        hheaDirOffset = currentPos;
        currentPos += 12;
        realSize += 16;

        writeString("hmtx");
        hmtxDirOffset = currentPos;
        currentPos += 12;
        realSize += 16;

        writeString("loca");
        locaDirOffset = currentPos;
        currentPos += 12;
        realSize += 16;

        writeString("maxp");
        maxpDirOffset = currentPos;
        currentPos += 12;
        realSize += 16;

        writeString("prep");
        prepDirOffset = currentPos;
        currentPos += 12;
        realSize += 16;
    }


    /** 
     * Copy the cvt table as is from original font to subset font
     *
     * @param in The input to read the CVT from
     * @throws IOException Indicates a failure to read the entry
     */
    private void createCvt(FontFileReader in) throws IOException {
        TTFDirTabEntry entry = (TTFDirTabEntry)dirTabs.get("cvt ");
        if (entry != null) {
            pad4();
            seekTab(in, "cvt ", 0);
            System.arraycopy(in.getBytes((int)entry.getOffset(), (int)entry.getLength()),
                             0, output, currentPos, (int)entry.getLength());

            int checksum = getCheckSum(currentPos, (int)entry.getLength());
            writeULong(cvtDirOffset, checksum);
            writeULong(cvtDirOffset + 4, currentPos);
            writeULong(cvtDirOffset + 8, (int)entry.getLength());
            currentPos += (int)entry.getLength();
            realSize += (int)entry.getLength();
        } else {
            throw new IOException("Can't find cvt table");
        }
    }

    /**
     * True if this subset has FPGM table
     * 
     * @return True if the subset has FPGM
     */
    private boolean hasFpgm() {
        return (dirTabs.get("fpgm") != null);
    }


    /** 
     * Copy the fpgm table as is from original font to subset font
     *
     * @param in The stream to read the FPGM table from
     * @throws IOException Indicates a failure to read the table
     */
    private void createFpgm(FontFileReader in) throws IOException {
        TTFDirTabEntry entry = (TTFDirTabEntry)dirTabs.get("fpgm");
        if (entry != null) {
            pad4();
            seekTab(in, "fpgm", 0);
            System.arraycopy(in.getBytes((int)entry.getOffset(), (int)entry.getLength()),
                             0, output, currentPos, (int)entry.getLength());
            int checksum = getCheckSum(currentPos, (int)entry.getLength());
            writeULong(fpgmDirOffset, checksum);
            writeULong(fpgmDirOffset + 4, currentPos);
            writeULong(fpgmDirOffset + 8, (int)entry.getLength());
            currentPos += (int)entry.getLength();
            realSize += (int)entry.getLength();
        } else {
            //fpgm table is optional
            //throw new IOException("Can't find fpgm table");
        }
    }



    /**
     * Create an empty loca table without updating checksum
     * 
     * @param size The size of the loca to create
     * @throws IOException Indicate a failure to store the loca
     */
    private void createLoca(int size) throws IOException {
        pad4();
        locaOffset = currentPos;
        writeULong(locaDirOffset + 4, currentPos);
        writeULong(locaDirOffset + 8, size * 4 + 4);
        currentPos += size * 4 + 4;
        realSize += size * 4 + 4;
    }


    /** 
     * Copy the maxp table as is from original font to subset font
     * and set num glyphs to size
     *
     * @param in The reader from which to obtain the info
     * @param size The size of the MAXP table to write
     * @throws IOException Indicates a failure to write
     */
    private void createMaxp(FontFileReader in, int size) throws IOException {
        TTFDirTabEntry entry = (TTFDirTabEntry)dirTabs.get("maxp");
        if (entry != null) {
            pad4();
            seekTab(in, "maxp", 0);
            System.arraycopy(in.getBytes((int)entry.getOffset(), (int)entry.getLength()),
                             0, output, currentPos, (int)entry.getLength());
            writeUShort(currentPos + 4, size);

            int checksum = getCheckSum(currentPos, (int)entry.getLength());
            writeULong(maxpDirOffset, checksum);
            writeULong(maxpDirOffset + 4, currentPos);
            writeULong(maxpDirOffset + 8, (int)entry.getLength());
            currentPos += (int)entry.getLength();
            realSize += (int)entry.getLength();
        } else {
            throw new IOException("Can't find maxp table");
        }
    }


    /**
     * Copy the prep table as is from original font to subset font
     *
     * @param in The reader to which we're adding
     * @throws IOException Indicates a failure to read the PREP table
     */
    private void createPrep(FontFileReader in) throws IOException {
        TTFDirTabEntry entry = (TTFDirTabEntry)dirTabs.get("prep");
        if (entry != null) {
            pad4();
            seekTab(in, "prep", 0);
            System.arraycopy(in.getBytes((int)entry.getOffset(), (int)entry.getLength()),
                             0, output, currentPos, (int)entry.getLength());

            int checksum = getCheckSum(currentPos, (int)entry.getLength());
            writeULong(prepDirOffset, checksum);
            writeULong(prepDirOffset + 4, currentPos);
            writeULong(prepDirOffset + 8, (int)entry.getLength());
            currentPos += (int)entry.getLength();
            realSize += (int)entry.getLength();
        } else {
            throw new IOException("Can't find prep table");
        }
    }


    /**
     * Copy the hhea table as is from original font to subset font
     * and fill in size of hmtx table
     *
     * @param in The reader from which to grab the HHEA table
     * @param size The size of the table
     * @throws IOException Indicates a failure to read data
     */
    private void createHhea(FontFileReader in, int size) throws IOException {
        TTFDirTabEntry entry = (TTFDirTabEntry)dirTabs.get("hhea");
        if (entry != null) {
            pad4();
            seekTab(in, "hhea", 0);
            System.arraycopy(in.getBytes((int)entry.getOffset(), (int)entry.getLength()),
                             0, output, currentPos, (int)entry.getLength());
            writeUShort((int)entry.getLength() + currentPos - 2, size);

            int checksum = getCheckSum(currentPos, (int)entry.getLength());
            writeULong(hheaDirOffset, checksum);
            writeULong(hheaDirOffset + 4, currentPos);
            writeULong(hheaDirOffset + 8, (int)entry.getLength());
            currentPos += (int)entry.getLength();
            realSize += (int)entry.getLength();
        } else {
            throw new IOException("Can't find hhea table");
        }
    }


    /**
     * Copy the head table as is from original font to subset font
     * and set indexToLocaFormat to long and set
     * checkSumAdjustment to 0, store offset to checkSumAdjustment
     * in checkSumAdjustmentOffset
     * 
     * @param in The reader to read the HEAD table from
     * @throws IOException Failure to read the table
     */
    private void createHead(FontFileReader in) throws IOException {
        TTFDirTabEntry entry = (TTFDirTabEntry)dirTabs.get("head");
        if (entry != null) {
            pad4();
            seekTab(in, "head", 0);
            System.arraycopy(in.getBytes((int)entry.getOffset(), (int)entry.getLength()),
                             0, output, currentPos, (int)entry.getLength());

            checkSumAdjustmentOffset = currentPos + 8;
            output[currentPos + 8] = 0;     // Set checkSumAdjustment to 0
            output[currentPos + 9] = 0;
            output[currentPos + 10] = 0;
            output[currentPos + 11] = 0;
            output[currentPos + 50] = 0;    // long locaformat
            output[currentPos + 51] = 1;    // long locaformat

            int checksum = getCheckSum(currentPos, (int)entry.getLength());
            writeULong(headDirOffset, checksum);
            writeULong(headDirOffset + 4, currentPos);
            writeULong(headDirOffset + 8, (int)entry.getLength());

            currentPos += (int)entry.getLength();
            realSize += (int)entry.getLength();
        } else {
            throw new IOException("Can't find head table");
        }
    }


    /**
     * Create the glyf table and fill in loca table
     *
     * @param in Reader to get the table from
     * @param glyphs The glpyhs table to populate
     * @throws IOException Indicates a failure to read data
     */
    private void createGlyf(FontFileReader in,
                            Map glyphs) throws IOException {
        TTFDirTabEntry entry = (TTFDirTabEntry)dirTabs.get("glyf");
        int size = 0;
        int start = 0;
        int endOffset = 0;    // Store this as the last loca
        if (entry != null) {
            pad4();
            start = currentPos;

            /* Loca table must be in order by glyph index, so build
             * an array first and then write the glyph info and
             * location offset.
             */
            int[] origIndexes = new int[glyphs.size()];

            Iterator e = glyphs.keySet().iterator();
            while (e.hasNext()) {
                Integer origIndex = (Integer)e.next();
                Integer subsetIndex = (Integer)glyphs.get(origIndex);
                origIndexes[subsetIndex.intValue()] = origIndex.intValue();
            }

            for (int i = 0; i < origIndexes.length; i++) {
                int glyphLength = 0;
                int nextOffset = 0;
                int origGlyphIndex = origIndexes[i];
                if (origGlyphIndex >= (mtxTab.length - 1)) {
                    nextOffset = (int)lastLoca;
                } else {
                    nextOffset = (int)mtxTab[origGlyphIndex + 1].getOffset();
                }
                glyphLength = nextOffset - (int)mtxTab[origGlyphIndex].getOffset();

                // Copy glyph
                System.arraycopy(
                    in.getBytes((int)entry.getOffset() + (int)mtxTab[origGlyphIndex].getOffset(),
                        glyphLength), 0,
                    output, currentPos,
                    glyphLength);


                // Update loca table
                writeULong(locaOffset + i * 4, currentPos - start);
                if ((currentPos - start + glyphLength) > endOffset) {
                    endOffset = (currentPos - start + glyphLength);
                }

                currentPos += glyphLength;
                realSize += glyphLength;

            }

            size = currentPos - start;

            int checksum = getCheckSum(start, size);
            writeULong(glyfDirOffset, checksum);
            writeULong(glyfDirOffset + 4, start);
            writeULong(glyfDirOffset + 8, size);
            currentPos += 12;
            realSize += 12;

            // Update loca checksum and last loca index
            writeULong(locaOffset + glyphs.size() * 4, endOffset);

            checksum = getCheckSum(locaOffset, glyphs.size() * 4 + 4);
            writeULong(locaDirOffset, checksum);
        } else {
            throw new IOException("Can't find glyf table");
        }
    }


    /**
     * Create the hmtx table by copying metrics from original
     * font to subset font. The glyphs Map contains an
     * Integer key and Integer value that maps the original
     * metric (key) to the subset metric (value)
     *
     * @param in The reader from which to grab the HMTX table
     * @param glyphs The glyphs table to populate 
     * @throws IOException Indicates a failure to read 
     */
    private void createHmtx(FontFileReader in,
                            Map glyphs) throws IOException {
        TTFDirTabEntry entry = (TTFDirTabEntry)dirTabs.get("hmtx");

        int longHorMetricSize = glyphs.size() * 2;
        int leftSideBearingSize = glyphs.size() * 2;
        int hmtxSize = longHorMetricSize + leftSideBearingSize;

        if (entry != null) {
            pad4();
            //int offset = (int)entry.offset;
            Iterator e = glyphs.keySet().iterator();
            while (e.hasNext()) {
                Integer origIndex = (Integer)e.next();
                Integer subsetIndex = (Integer)glyphs.get(origIndex);

                writeUShort(currentPos + subsetIndex.intValue() * 4,
                            mtxTab[origIndex.intValue()].getWx());
                writeUShort(currentPos + subsetIndex.intValue() * 4 + 2,
                            mtxTab[origIndex.intValue()].getLsb());
            }

            int checksum = getCheckSum(currentPos, hmtxSize);
            writeULong(hmtxDirOffset, checksum);
            writeULong(hmtxDirOffset + 4, currentPos);
            writeULong(hmtxDirOffset + 8, hmtxSize);
            currentPos += hmtxSize;
            realSize += hmtxSize;
        } else {
            throw new IOException("Can't find hmtx table");
        }
    }

    /**
     * Returns a List containing the glyph itself plus all glyphs
     * that this composite glyph uses
     * 
     * @param in The input from which to determine the included glyphs
     * @param glyphOffset The offset the glyph
     * @param glyphIdx The index of the base glyph
     * @return The list of glyphs building the composite
     * @throws IOException Indicates a failure to read from the font file
     */
    private List getIncludedGlyphs(FontFileReader in, int glyphOffset,
                                     Integer glyphIdx) throws IOException {
        List ret = new ArrayList();
        ret.add(glyphIdx);
        int offset = glyphOffset + (int)mtxTab[glyphIdx.intValue()].getOffset() + 10;
        Integer compositeIdx = null;
        int flags = 0;
        boolean moreComposites = true;
        while (moreComposites) {
            flags = in.readTTFUShort(offset);
            compositeIdx = new Integer(in.readTTFUShort(offset + 2));
            ret.add(compositeIdx);

            offset += 4;
            if ((flags & 1) > 0) {
                // ARG_1_AND_ARG_2_ARE_WORDS
                offset += 4;
            } else {
                offset += 2;
            }

            if ((flags & 8) > 0) {
                offset += 2;    // WE_HAVE_A_SCALE
            } else if ((flags & 64) > 0) {
                offset += 4;    // WE_HAVE_AN_X_AND_Y_SCALE
            } else if ((flags & 128) > 0) {
                offset += 8;    // WE_HAVE_A_TWO_BY_TWO
            }

            if ((flags & 32) > 0) {
                moreComposites = true;
            } else {
                moreComposites = false;
            }
        }

        return ret;
    }


    /**
     * Rewrite all compositepointers in glyphindex glyphIdx
     * 
     * @param in The input from which to remap 
     * @param glyphs The glyphs to remap
     * @param glyphOffset The offset to start at
     * @param glyphIdx The index of the glyph
     * @throws IOException Indicates a failure to read from the font file.
     */
    private void remapComposite(FontFileReader in, Map glyphs,
                                int glyphOffset,
                                Integer glyphIdx) throws IOException {
        int offset = glyphOffset + (int)mtxTab[glyphIdx.intValue()].getOffset()
                     + 10;

        Integer compositeIdx = null;
        int flags = 0;
        boolean moreComposites = true;

        while (moreComposites) {
            flags = in.readTTFUShort(offset);
            compositeIdx = new Integer(in.readTTFUShort(offset + 2));
            Integer newIdx = (Integer)glyphs.get(compositeIdx);
            if (newIdx == null) {
                // This errormessage would look much better
                // if the fontname was printed to
                //log.error("An embedded font "
                //                     + "contains bad glyph data. "
                //                     + "Characters might not display "
                //                     + "correctly.");
                moreComposites = false;
                continue;
            }

            in.writeTTFUShort(offset + 2, newIdx.intValue());

            offset += 4;

            if ((flags & 1) > 0) {
                // ARG_1_AND_ARG_2_ARE_WORDS
                offset += 4;
            } else {
                offset += 2;
            }

            if ((flags & 8) > 0) {
                offset += 2;    // WE_HAVE_A_SCALE
            } else if ((flags & 64) > 0) {
                offset += 4;    // WE_HAVE_AN_X_AND_Y_SCALE
            } else if ((flags & 128) > 0) {
                offset += 8;    // WE_HAVE_A_TWO_BY_TWO
            }

            if ((flags & 32) > 0) {
                moreComposites = true;
            } else {
                moreComposites = false;
            }
        }
    }


    /**
     * Scan all the original glyphs for composite glyphs and add those glyphs
     * to the glyphmapping also rewrite the composite glyph pointers to the new
     * mapping
     * 
     * @param in The input stream to read the glyphs from
     * @param glyphs The glyphs map to populate
     * @throws IOException Indicates a failure to read from the reader
     */
    private void scanGlyphs(FontFileReader in,
                            Map glyphs) throws IOException {
        TTFDirTabEntry entry = (TTFDirTabEntry)dirTabs.get("glyf");
        Map newComposites = null;
        Map allComposites = new java.util.HashMap();

        int newIndex = glyphs.size();

        if (entry != null) {
            while (newComposites == null || newComposites.size() > 0) {
                // Inefficient to iterate through all glyphs
                newComposites = new java.util.HashMap();

                Iterator e = glyphs.keySet().iterator();
                while (e.hasNext()) {
                    Integer origIndex = (Integer)e.next();

                    if (in.readTTFShort(entry.getOffset()
                                        + mtxTab[origIndex.intValue()].getOffset()) < 0) {
                        // origIndex is a composite glyph
                        allComposites.put(origIndex, glyphs.get(origIndex));
                        List composites =
                            getIncludedGlyphs(in, (int)entry.getOffset(),
                                              origIndex);

                        // Iterate through all composites pointed to
                        // by this composite and check if they exists
                        // in the glyphs map, add them if not.
                        Iterator cps = composites.iterator();
                        while (cps.hasNext()) {
                            Integer cIdx = (Integer)cps.next();
                            if (glyphs.get(cIdx) == null
                                    && newComposites.get(cIdx) == null) {
                                newComposites.put(cIdx,
                                                  new Integer(newIndex));
                                newIndex++;
                            }
                        }
                    }
                }

                // Add composites to glyphs
                Iterator m = newComposites.keySet().iterator();
                while (m.hasNext()) {
                    Integer im = (Integer)m.next();
                    glyphs.put(im, newComposites.get(im));
                }
            }

            // Iterate through all composites to remap their composite index
            Iterator ce = allComposites.keySet().iterator();
            while (ce.hasNext()) {
                remapComposite(in, glyphs, (int)entry.getOffset(),
                               (Integer)ce.next());
            }

        } else {
            throw new IOException("Can't find glyf table");
        }
    }



    /**
     * Returns a subset of the original font.
     *
     * @param in FontFileReader to read from
     * @param name Name to be checked for in the font file
     * @param glyphs Map of glyphs (glyphs has old index as (Integer) key and
     * new index as (Integer) value)
     * @return A subset of the original font
     * @throws IOException in case of an I/O problem
     */
    public byte[] readFont(FontFileReader in, String name,
                           Map glyphs) throws IOException {

        //Check if TrueType collection, and that the name exists in the collection
        if (!checkTTC(in, name)) {
            throw new IOException("Failed to read font");
        }

        output = new byte[in.getFileSize()];

        readDirTabs(in);
        readFontHeader(in);
        getNumGlyphs(in);
        readHorizontalHeader(in);
        readHorizontalMetrics(in);
        readIndexToLocation(in);

        scanGlyphs(in, glyphs);

        createDirectory();                // Create the TrueType header and directory

        createHead(in);
        createHhea(in, glyphs.size());    // Create the hhea table
        createHmtx(in, glyphs);           // Create hmtx table
        createMaxp(in, glyphs.size());    // copy the maxp table

        try {
            createCvt(in);    // copy the cvt table
        } catch (IOException ex) {
            // Cvt is optional (only required for OpenType (MS) fonts)
            //log.error("TrueType warning: " + ex.getMessage());
        }

        try {
            createFpgm(in);    // copy fpgm table
        } catch (IOException ex) {
            // Fpgm is optional (only required for OpenType (MS) fonts)
            //log.error("TrueType warning: " + ex.getMessage());
        }

        try {
            createPrep(in);    // copy prep table
        } catch (IOException ex) {
            // Prep is optional (only required for OpenType (MS) fonts)
            //log.error("TrueType warning: " + ex.getMessage());
        }

        try {
            createLoca(glyphs.size());    // create empty loca table
        } catch (IOException ex) {
            // Loca is optional (only required for OpenType (MS) fonts)
            //log.error("TrueType warning: " + ex.getMessage());
        }

        try {
            createGlyf(in, glyphs);
        } catch (IOException ex) {
            // Glyf is optional (only required for OpenType (MS) fonts)
            //log.error("TrueType warning: " + ex.getMessage());
        }

        pad4();
        createCheckSumAdjustment();

        byte[] ret = new byte[realSize];
        System.arraycopy(output, 0, ret, 0, realSize);

        return ret;
    }

    /**
     * writes a ISO-8859-1 string at the currentPosition
     * updates currentPosition but not realSize
     * @return number of bytes written
     * 
     * @param str Write a string at the current position
     */
    private int writeString(String str) {
        int length = 0;
        try {
            byte[] buf = str.getBytes("ISO-8859-1");
            System.arraycopy(buf, 0, output, currentPos, buf.length);
            length = buf.length;
            currentPos += length;
        } catch (UnsupportedEncodingException e) {
            // This should never happen!
        }

        return length;
    }

    /**
     * Appends a byte to the output array,
     * updates currentPost but not realSize
     * 
     * @param b The byte value to write out
     */
    private void writeByte(byte b) {
        output[currentPos++] = b;
    }

    /**
     * Appends a USHORT to the output array,
     * updates currentPost but not realSize
     *
     * @param s The short to write
     */
    private void writeUShort(int s) {
        byte b1 = (byte)((s >> 8) & 0xff);
        byte b2 = (byte)(s & 0xff);
        writeByte(b1);
        writeByte(b2);
    }

    /** 
     * Appends a USHORT to the output array,
     * at the given position without changing currentPos
     *
     * @param pos The position to write to
     * @param s The short to be written
     */
    private void writeUShort(int pos, int s) {
        byte b1 = (byte)((s >> 8) & 0xff);
        byte b2 = (byte)(s & 0xff);
        output[pos] = b1;
        output[pos + 1] = b2;
    }

    /**
     * Appends a ULONG to the output array,
     * updates currentPos but not realSize
     *
     * @param s The value to write
     */
    private void writeULong(int s) {
        byte b1 = (byte)((s >> 24) & 0xff);
        byte b2 = (byte)((s >> 16) & 0xff);
        byte b3 = (byte)((s >> 8) & 0xff);
        byte b4 = (byte)(s & 0xff);
        writeByte(b1);
        writeByte(b2);
        writeByte(b3);
        writeByte(b4);
    }

    /**
     * 
     * Appends a ULONG to the output array,
     * at the given position without changing currentPos
     *
     * @param pos The position to write to
     * @param s The value to write
     */
    private void writeULong(int pos, int s) {
        byte b1 = (byte)((s >> 24) & 0xff);
        byte b2 = (byte)((s >> 16) & 0xff);
        byte b3 = (byte)((s >> 8) & 0xff);
        byte b4 = (byte)(s & 0xff);
        output[pos] = b1;
        output[pos + 1] = b2;
        output[pos + 2] = b3;
        output[pos + 3] = b4;
    }

    /**
     * Read a signed short value at given position
     *
     * @param pos The position from the file to read
     * @return The short read
     */
    private short readShort(int pos) {
        int ret = readUShort(pos);
        return (short)ret;
    }

    /**
     * Read a unsigned short value at given position
     *
     * @param pos The position from the file to read
     * @return The short read
     */
    private int readUShort(int pos) {
        int ret = output[pos];
        if (ret < 0) {
            ret += 256;
        }
        ret = ret << 8;
        if (output[pos + 1] < 0) {
            ret |= output[pos + 1] + 256;
        } else {
            ret |= output[pos + 1];
        }

        return ret;
    }

    /**
     * Create a padding in the fontfile to align
     * on a 4-byte boundary
     */
    private void pad4() {
        int padSize = currentPos % 4;
        for (int i = 0; i < padSize; i++) {
            output[currentPos++] = 0;
            realSize++;
        }
    }

    /**
     * Returns the maximum power of 2 <= max
     * 
     * @param max The value to find the maximum power of 2
     * @return The maximum power of 2
     */
    private int maxPow2(int max) {
        int i = 0;
        while (Math.pow(2, i) < max) {
            i++;
        }

        return (i - 1);
    }

    /**
     * Perform a log2
     * 
     * @param num The number to log2
     * @return The log2 value
     */
    private int log2(int num) {
        return (int)(Math.log(num) / Math.log(2));
    }


    /**
     * Get the checksum for this entry
     * 
     * @param start The start of the value
     * @param size The size of the value
     * @return The calculated check sum
     */
    private int getCheckSum(int start, int size) {
        return (int)getLongCheckSum(start, size);
    }

    /**
     * Get the checksum as a long
     * 
     * @param start The start value 
     * @param size The size of the values to checksum
     * @return The long checksum
     */
    private long getLongCheckSum(int start, int size) {
        // All the tables here are aligned on four byte boundaries
        // Add remainder to size if it's not a multiple of 4
        int remainder = size % 4;
        if (remainder != 0) {
            size += remainder;
        }

        long sum = 0;

        for (int i = 0; i < size; i += 4) {
            int l = (output[start + i] << 24);
            l += (output[start + i + 1] << 16);
            l += (output[start + i + 2] << 16);
            l += (output[start + i + 3] << 16);
            sum += l;
            if (sum > 0xffffffff) {
                sum = sum - 0xffffffff;
            }
        }

        return sum;
    }

    /**
     * Create the checksum adjustment
     */
    private void createCheckSumAdjustment() {
        long sum = getLongCheckSum(0, realSize);
        int checksum = (int)(0xb1b0afba - sum);
        writeULong(checkSumAdjustmentOffset, checksum);
    }

}



