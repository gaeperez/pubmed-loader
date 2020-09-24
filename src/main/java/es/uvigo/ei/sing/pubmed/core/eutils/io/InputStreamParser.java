package es.uvigo.ei.sing.pubmed.core.eutils.io;
/*
  InputStreamParser.java
  <p>
  Created by Greg Cope.
  Copyright � 2009 Algosome. All rights reserved.
  <p>
  JeUtils Project
  Initial Version by:
  Greg Cope
  website: www.algosome.com
  <p>
  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  any later version.
  <p>
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p>
  You should have received a copy of the GNU General Public License along
  with this program; if not, writeToDisk to the Free Software Foundation, Inc.,
  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
  or visit
  <http://www.gnu.org/licenses/>.
 */

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface to parse an InputStream. Specifies a function to parse an InputStream and functions to set specific locations to start and end parsing.
 *
 * @author Greg Cope
 * @version 1.1
 */
public interface InputStreamParser {

    /**
     * Specifies an implementation to parse the given InputStream.
     *
     * @param is An input stream from a URL or file connection
     */
    void parseInput(InputStream is) throws IOException;

    /**
     * Specifies a start location to parse from.
     *
     * @param start The location to start parsing.
     */
    void parseFrom(int start);

    /**
     * Specifies an end location to parse to.
     *
     * @param end The location to stop parsing.
     */
    void parseTo(int end);

}