package org.lilbrocodes.conductor.api.identifier;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("unused")
public class Identifier implements Comparable<Identifier> {
    public static final char NAMESPACE_SEPARATOR = ':';
    protected final String namespace;
    protected final String path;

    public Identifier(String id) {
        this(split(id, NAMESPACE_SEPARATOR));
    }

    public Identifier(String namespace, String path) {
        this(new String[]{namespace, path});
    }

    private Identifier(String[] id) {
        this.namespace = id[0];
        this.path = id[0];
    }

    @SuppressWarnings("all")
    protected static String[] split(String id, char delimiter) {
        String[] astring = new String[]{"minecraft", id};
        int i = id.indexOf(delimiter);
        if (i >= 0) {
            astring[1] = id.substring(i + 1);
            if (i >= 1) {
                astring[0] = id.substring(0, i);
            }
        }

        return astring;
    }

    public String getPath() {
        return this.path;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String toString() {
        return this.namespace + NAMESPACE_SEPARATOR + this.path;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Identifier resourceLocation)) {
            return false;
        } else {
            return this.namespace.equals(resourceLocation.namespace) && this.path.equals(resourceLocation.path);
        }
    }

    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }

    public int compareTo(Identifier arg) {
        int i = this.path.compareTo(arg.path);
        if (i == 0) {
            i = this.namespace.compareTo(arg.namespace);
        }

        return i;
    }

    public int compareNamespaced(Identifier o) {
        int ret = this.namespace.compareTo(o.namespace);
        return ret != 0 ? ret : this.path.compareTo(o.path);
    }

    public String toUnderscoreSeparatedString() {
        return this.toString().replace('/', '_').replace(NAMESPACE_SEPARATOR, '_');
    }

    public static boolean isCharValid(char c) {
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c == '_' || c == ':' || c == '/' || c == '.' || c == '-';
    }

    private static boolean isPathValid(String path) {
        for(int i = 0; i < path.length(); ++i) {
            if (!isPathCharacterValid(path.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    private static boolean isNamespaceValid(String namespace) {
        for(int i = 0; i < namespace.length(); ++i) {
            if (!isNamespaceCharacterValid(namespace.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isPathCharacterValid(char character) {
        return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '/' || character == '.';
    }

    private static boolean isNamespaceCharacterValid(char character) {
        return character == '_' || character == '-' || character >= 'a' && character <= 'z' || character >= '0' && character <= '9' || character == '.';
    }

    public static boolean isValid(String id) {
        String[] astring = split(id, NAMESPACE_SEPARATOR);
        return isNamespaceValid(StringUtils.isEmpty(astring[0]) ? "minecraft" : astring[0]) && isPathValid(astring[1]);
    }
}
