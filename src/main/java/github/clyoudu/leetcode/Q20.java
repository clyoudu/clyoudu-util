package github.clyoudu.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author leichen
 */
public class Q20 {

    public static void main(String[] args) {
        System.out.println(isValid("()"));
        System.out.println(isValid("()[]{}"));
        System.out.println(isValid("()[]{"));
        System.out.println(isValid("()[]{]"));
        System.out.println(isValid("]"));
    }

    private static boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '{':
                case '(':
                case '[':
                    stack.push(c);
                    break;
                case '}':
                    if(stack.isEmpty() || stack.pop() != '{'){
                        return false;
                    }
                    break;
                case ')':
                    if(stack.isEmpty() || stack.pop() != '('){
                        return false;
                    }
                    break;
                case ']':
                    if(stack.isEmpty() || stack.pop() != '['){
                        return false;
                    }
                    break;
                default:
                    throw new IllegalArgumentException(c + "");
            }
        }

        return stack.isEmpty();
    }

}
