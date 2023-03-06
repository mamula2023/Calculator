package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Controller {
	private String operators = "+-*/";
	@FXML
	private GridPane numberGrid;
	@FXML
	private Button one;
	@FXML
	private Button two;
	@FXML
	private Button three;
	@FXML
	private Button four;
	@FXML
	private Button five;
	@FXML
	private Button six;
	@FXML
	private Button seven;
	@FXML
	private Button eight;
	@FXML
	private Button nine;
	@FXML
	private Button zero;
	@FXML
	private Button plus;
	@FXML
	private Button minus;
	@FXML
	private Button equalsButton;
	@FXML
	private Button clearButton;
	@FXML
	private Button open;
	@FXML
	private Button close;
	@FXML
	private Button mult;
	@FXML
	private TextArea output;
	@FXML
	private Button dot;
	@FXML
	private Button divide;

	@FXML
	void equals(ActionEvent event) {
		String equation = output.getText();
		if (equation.trim().equals(""))
			return;
		String result = calculate(equation);
		result = equation + '=' + result;
		output.setText(result);
		
		
	}

	/*
	 * calculates equation without any parenthesis currently, multiplication,
	 * addition, subtraction
	 */
	private String calculate(String eq) {
		eq = chainOfOperations(eq, "*/");
		eq = chainOfOperations(eq, "+-");
		return eq;
	}

	private String chainOfOperations(String eq, String operator) {
		String result = "";
		int i = 0;
		while (true) {
			if (i == eq.length() - 1) {
				break;
			}
			char ch = eq.charAt(i);
			if (operator.indexOf(ch) != -1) {
				String prevNumber = lastNumber(eq.substring(0, i));
				String nextNumber = firstNumber(eq.substring(i + 1));
				if (ch == '*')
					result = multiply(prevNumber, nextNumber);
				else if (ch == '+')
					result = add(prevNumber, nextNumber);
				else if (ch == '-')
					result = substract(prevNumber, nextNumber);

				int prevOp = i;
				for (int j = i - 1; j >= 0; j--) {
					if (operators.indexOf(eq.charAt(j)) != -1) {
						prevOp = j;
						break;
					}
				}

				int nextOp = i;
				for (int j = i + 1; j < eq.length(); j++) {
					if (operators.indexOf(eq.charAt(j)) != -1) {
						nextOp = j;
						break;
					}
				}

				if (prevOp != i && nextOp != i) {
					eq = eq.substring(0, prevOp + 1) + result + eq.substring(nextOp);
					i = prevOp;
				} else if (prevOp == i && nextOp != i) {
					eq = result + eq.substring(nextOp);
					i = result.length() - 1;
				} else if (prevOp != i && nextOp == i) {
					eq = eq.substring(0, prevOp + 1) + result;
					i = prevOp;

				} else {
					eq = result;
					i = result.length() - 2;

				}
			}

			i++;

		}

		return eq;
	}

	public void writeFromKey(char ch) {
		if (ch >= '0' && ch <= '9') {
			String result = "";
			result += ch;
			addText(result);
		} else if (operators.indexOf(ch) != -1) {
			String currentOutput = output.getText();
			if (!currentOutput.equals("")) {
				String operator = currentOutput.substring(currentOutput.length() - 1);
				if (operators.contains(operator))
					currentOutput = currentOutput.substring(0, currentOutput.length() - 1);
			}
			currentOutput += ch;
			output.setText(currentOutput);
		}

	}

	@FXML
	void write(ActionEvent event) {
		String input = ((Button) event.getTarget()).getText();
		addText(input);
	}

	@FXML
	void operator(ActionEvent event) {
		String input = ((Button) event.getTarget()).getText();
		String currentOutput = output.getText();
		if (!currentOutput.equals("")) {
			String operator = currentOutput.substring(currentOutput.length() - 1);
			if (operators.contains(operator))
				currentOutput = currentOutput.substring(0, currentOutput.length() - 1);
		}
		currentOutput += input;
		output.setText(currentOutput);
	}

	private void addText(String text) {
		String currentOutput = output.getText();
		currentOutput += text;
		output.setText(currentOutput);
	}

	private String add(String num1, String num2) {
		if (num1.indexOf('.') == -1)
			num1 += ".0";
		if (num2.indexOf('.') == -1)
			num2 += ".0";

		String whole1 = num1.substring(0, num1.indexOf('.'));
		String decimal1 = num1.substring(num1.indexOf('.') + 1);
		String whole2 = num2.substring(0, num2.indexOf('.'));
		String decimal2 = num2.substring(num2.indexOf('.') + 1);

		if (decimal2.length() > decimal1.length()) {
			String temp = decimal1;
			decimal1 = decimal2;
			decimal2 = temp;
		}

		int diff = decimal1.length() - decimal2.length();
		for (int i = 0; i < diff; i++) 
			decimal2 = decimal2 + '0';
		
		String wholeSum = removeLeadingZeros(addIntegers(whole1, whole2));
		String decimalSum = addDecimals(decimal1, decimal2);
	
	
		String result = "";
		if(decimalSum.charAt(0)=='0')
			result =  wholeSum+decimalSum.substring(1);
		else 
			result = addIntegers(wholeSum, "1")+decimalSum.substring(1);
		
		result = removeLeadingZeros(result);
		result = removeLastZeros(result);
		return result;
	}

	private String addDecimals(String num1, String num2) {
		ArrayList<Integer> n1 = toArrayList(num1);
		ArrayList<Integer> n2 = toArrayList(num2);
		String result = "";
		int carry = 0;
		for (int i = 0; i < n1.size(); i++) {
			int sum = n1.get(n1.size() - 1 - i) + n2.get(n2.size() - 1 - i) + carry;
			carry = sum / 10;
			result = (char)(sum%10+'0')+result;
		}
		char ch = (char)(carry+'0');
		result = '.'+result;
		result = ch+result;
		return result;
	}

	private String addIntegers(String num1, String num2) {
		ArrayList<Integer> res = new ArrayList<>();
		ArrayList<Integer> n1 = toArrayList(num1);
		ArrayList<Integer> n2 = toArrayList(num2);

		if (n2.size() > n1.size()) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp = n1;
			n1 = n2;
			n2 = temp;
		}

		int carry = 0;
		for (int i = 0; i < n1.size(); i++) {
			int sum = n1.get(n1.size() - 1 - i) + carry;
			if (i < n2.size())
				sum += n2.get(n2.size() - 1 - i);
			carry = sum / 10;
			res.add(0, sum % 10);
		}
		if (carry != 0)
			res.add(0, carry);

		return ALtoString(res);
	}

	// used if only one number is negative,
	// if both numbers are negative, add absolute values and add minus;
	// num2 is negative number;
	private String substract(String num1, String num2) {
		ArrayList<Integer> res = new ArrayList<>();
		boolean swapped = false;

		int comparision = compare(num1, num2);
		num1 = removeLeadingZeros(num1);
		num2 = removeLeadingZeros(num2);
		if (comparision == -1) {
			String temp = num1;
			num1 = num2;
			num2 = temp;
			swapped = true;
		}

		ArrayList<Integer> n1 = toArrayList(num1);
		ArrayList<Integer> n2 = toArrayList(num2);

		int diff = n1.size() - n2.size();

		for (int i = 0; i < diff; i++)
			n2.add(0, 0);

		int carry = 0;

		for (int i = n1.size() - 1; i >= 0; i--) {
			int s = n1.get(i) - n2.get(i) - carry;
			if (s < 0) {
				carry = 1;
				res.add(0, 10 + s);
			} else {
				carry = 0;
				res.add(0, s);
			}

		}

		String result = ALtoString(res);

		if ((swapped && carry == 0) || (!swapped && carry == 1))
			result = '-' + result;

		return removeLeadingZeros(result);
	}

	private String multiply(String num1, String num2) {
		int i1 = num1.indexOf('.');
		int i2 = num2.indexOf('.');
		
		if (i1 == -1) {
			num1 += ".0";
			i1 = num1.indexOf('.');
		}
		if (i2 == -1) {
			num2 += ".0";
			i2 = num2.indexOf('.');
		}
		ArrayList<Integer> n1 = toArrayList(num1);
		ArrayList<Integer> n2 = toArrayList(num2);
		
		String result = multiplyInteger(n1, n2);
		int i = num1.length()-1-i1+num2.length()-1-i2;
		
	
		
		result = result.substring(0, result.length()-i)+'.'+result.substring(result.length()-i);
		

		return removeLeadingZeros(removeLastZeros(result));
	}
	
	private String multiplyInteger(ArrayList<Integer> n1, ArrayList<Integer> n2) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		if (n2.size() > n1.size()) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp = n1;
			n1 = n2;
			n2 = temp;
		}

		int diff = n1.size() - n2.size();
		for (int i = 0; i < diff; i++)
			n2.add(0, 0);

		ArrayList<ArrayList<Integer>> inBetween = new ArrayList<>();

		for (int i = 0; i < n2.size(); i++) {
			ArrayList<Integer> n1ToOneDigit = new ArrayList<>();
			n1ToOneDigit = multiplyDigitToNum(n1, n2.get(n2.size() - 1 - i));
			for (int j = 0; j < i; j++)
				n1ToOneDigit.add(0);
			inBetween.add(n1ToOneDigit);
		}

		int carry = 0;
		for (int i = 0; i < inBetween.get(inBetween.size() - 1).size(); i++) {
			int sum = 0;
			for (int j = 0; j < inBetween.size(); j++) {
				if (inBetween.get(j).size() > i)
					sum += inBetween.get(j).get(inBetween.get(j).size() - 1 - i);
			}
			sum += carry;
			res.add(0, sum % 10);
			carry = sum / 10;
		}
		return ALtoString(res);
	}
	
	

	private ArrayList<Integer> multiplyDigitToNum(ArrayList<Integer> n1, int num) {
		ArrayList<Integer> result = new ArrayList<>();
		int carry = 0;
		for (int i = 0; i < n1.size(); i++) {
			int multiplication = num * n1.get(n1.size() - 1 - i) + carry;
			result.add(0, multiplication % 10);
			carry = multiplication / 10;
		}
		if (carry != 0)
			result.add(0, carry);

		return result;
	}

	private String removeLeadingZeros(String num) {
		while (num.length() > 1) {
			if (num.charAt(0) == '0')
				num = num.substring(1);
			else
				break;
		}
		return num;
	}

	private String removeLastZeros(String num) {
		while(true) {
			if(num.charAt(num.length()-1)=='0')
				num = num.substring(0, num.length()-1);
			else break;
		}
		
		if(num.charAt(num.length()-1)=='.')
			num = num.substring(0, num.length()-1);
		
		return num;
	}
	/*
	 * clears output of calculator;
	 */
	@FXML
	void clear(ActionEvent event) {
		output.setText("");
	}

	/*
	 * method to identify first number in substring of equation. for example,
	 * firstNumber("234+4234+432*576") will return "234"
	 */
	private String firstNumber(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (operators.indexOf(str.charAt(i)) != -1) {
				return str.substring(0, i);
			}
		}
		return str;
	}

	/*
	 * method to identify last number in substring of equation. for example,
	 * lastNumber("234+4234+432*576") will return "576"
	 */
	private String lastNumber(String str) {
		boolean changed = false;
		int index = 0;
		for (int i = 0; i < str.length(); i++) {
			if (operators.indexOf(str.charAt(i)) != -1) {
				index = i;
				changed = true;
			}
		}

		if (!changed)
			return str;

		return str.substring(index + 1);
	}

	/*
	 * this is method returns String which is built from arraylist, both
	 * representing number
	 */
	private String ALtoString(ArrayList<Integer> num) {
		String result = "";
		for (int i = 0; i < num.size(); i++) {
			char ch = (char) (num.get(i) + '0');
			result += ch;
		}
		return result;
	}

	/*
	 * this is method to transform String to ArrayList, both representis number,
	 * which represents number;
	 */
	private ArrayList<Integer> toArrayList(String num) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < num.length(); i++) {
			char ch = num.charAt(i);
			if(ch>='0'&& ch<='9') {
				int digit = ch - '0';
				result.add(digit);
			}else
				continue;
		}
		return result;
	}

	/*
	 * 1 if num1>num2 0 if num1=num2 -1 if num1<num2
	 */
	private int compare(String num1, String num2) {
		System.out.println("called");
		boolean pos1 = num1.charAt(0)!='-';
		boolean pos2 = num2.charAt(0)!='-';
		int result = 0;
		num1 = removeLeadingZeros(num1);
		num2 = removeLeadingZeros(num2);
		if (num1.length() > num2.length())
			result = 1;
		else if (num1.length() < num2.length())
			result = -1;
		else {
			for (int i = 0; i < num1.length(); i++) {
				if (num1.charAt(i) > num2.charAt(i))
					result = 1;
				else if (num1.charAt(i) < num2.charAt(i))
					result = -1;
			}

			result = 0;
		}
		
		if(pos1&&pos2)
			return result;
		else if(!pos1&&!pos2)
			return result*-1;
		else if(pos1)
			return 1;
		
		return -1;

	}
}
