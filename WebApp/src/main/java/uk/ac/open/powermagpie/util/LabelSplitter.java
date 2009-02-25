package uk.ac.open.powermagpie.util;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

/**

 * Utility class that splits labels.

 * @author Marta Sabou

 *

 */
public class LabelSplitter {

    /**

     * Splits a label into constituents.

     * For now returns a string where components are split by "/".

     * @param label

     * @return

     */
    public String splitLabel(String label) {

        String result = "";



        //first separate based on separators

        int firstSeparator = containsSeparator(label);



        if (firstSeparator > -1) {

            result = splitOnSeparators(label);

        } else {

            if (toSplit(label)) {

                result = splitOnCaps(label);

            } else {

                result = label;

            }

        }



        //System.out.println(result);

        return result;

    }

    @SuppressWarnings("static-access")
    public String splitOnCaps(String label) {

        String result = "";

        //current, previous and next characters.

        Character prev;

        Character curr = null;

        Character next = null;

        String element = "";



        for (int i = 0; i < label.length() - 1; i++) {

            prev = curr;
            curr = new Character(label.charAt(i));

            next = new Character(label.charAt(i + 1));



            if (curr.isUpperCase(curr.charValue())) {

                //first character of the term is an upper case

                if (prev == null) {

                    element = element + curr.toString();

                } else {

                    if (prev.isUpperCase(prev.charValue())) {

                        if (next.isUpperCase(next.charValue())) {

                            // a capital between different capitals, belongs to the same element

                            element = element + curr.toString();

                        } else {

                            //next is a lower case

                            //then add the element to the result

                            result = result + element + "/";

                            //and start a new element from current

                            element = curr.toString();

                        }

                    } else {

                        //previous is a lower case, end element

                        result = result + element + "/";

                        //and start a new element from current

                        element = curr.toString();

                    }

                }

            } else {

                //current is lower case

                element = element + curr.toString();

            }

        }



        //add last character

        element = element + next.toString();

        result = result + element + "/";



        return result;

    }

    /**

     * Splits only on separators.

     * @param label

     * @return

     */
    public String splitOnSeparators(String label) {

        String result = "";

        String element;



        int firstSeparator = containsSeparator(label);

        while (firstSeparator > -1) {

            element = label.substring(0, firstSeparator);

            //splits on caps if it is the case

            if (toSplit(element)) {

                element = this.splitOnCaps(element);

            }

            result = result + element + "/";

            label = label.substring(firstSeparator + 1, label.length());

            firstSeparator = containsSeparator(label);

        }



        if (toSplit(label)) {

            label = this.splitOnCaps(label);

        }

        result = result + label + "/";



        return result;

    }

    //tests is a label contains any of the {_, -, ., , +} separators

    //and returns the index of the first such separator
    public int containsSeparator(String label) {

        int result = -1;

        int indexOf_, indexOfMinus, indexOfDot, indexOfSpace, indexOfPlus;



        indexOf_ = label.indexOf("_");

        if (indexOf_ > -1) {

            result = indexOf_;

        }



        indexOfMinus = label.indexOf("-");

        if (indexOfMinus > -1) {

            if (result != -1) {

                if (indexOfMinus < result) {
                    result = indexOfMinus;
                }

            } else {

                result = indexOfMinus;

            }



        }



        indexOfDot = label.indexOf(".");

        if (indexOfDot > -1) {

            if (result != -1) {

                if (indexOfDot < result) {
                    result = indexOfDot;
                }

            } else {

                result = indexOfDot;

            }

        }



        indexOfSpace = label.indexOf(" ");

        if (indexOfSpace > -1) {

            if (result != -1) {

                if (indexOfSpace < result) {
                    result = indexOfSpace;
                }

            } else {

                result = indexOfSpace;

            }

        }



        indexOfPlus = label.indexOf("+");

        if (indexOfPlus > -1) {

            if (result != -1) {

                if (indexOfPlus < result) {
                    result = indexOfPlus;
                }

            } else {

                result = indexOfPlus;

            }

        }



        return result;

    }

    /**

     * Decides whether the given label should be split based on caps.

     * @param label

     * @return

     */
    public boolean toSplit(String label) {



        //split criteria

        //there is at least one upper case but not on first position

        //there is at least one lower case



        Pattern oneLowerCase = Pattern.compile("[a-z]+");

        Matcher m1 = oneLowerCase.matcher(label);

        Pattern upperCase = Pattern.compile(".+[A-Z]+");

        Matcher m2 = upperCase.matcher(label);



        return m1.find() && m2.find();



    }

    /**

     * Checks if a term is part of a compound label 

     * (where "/" separate the different parts of the label)

     * @param term

     * @param label

     * @return

     */
    public boolean isPartOfLabel(String term, String label) {

        boolean result = false;



        String element;

        while (label.indexOf("/") > -1) {

            element = label.substring(0, label.indexOf("/"));

            label = label.substring(label.indexOf("/") + 1, label.length());

            if (term.compareToIgnoreCase(element) == 0) {

                result = true;

            }

        }



        if (term.compareToIgnoreCase(label) == 0) {

            result = true;

        }



        return result;

    }
}

