/*******************************************************************************
 * Copyright (c) 2015 Vienna University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Martin Fleck (Vienna University of Technology) - initial API and implementation
 *
 * Initially developed in the context of ARTIST EU project www.artist-project.eu
 *******************************************************************************/
package at.ac.tuwien.big.moea.solution.printer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;

import at.ac.tuwien.big.moea.variable.IPlaceholderVariable;

public class GenericSolutionPrinter<S extends Solution> implements ISolutionPrinter<S> {	
	private Map<Integer, String> objectiveNames = new HashMap<>();
	private Map<Integer, String> variableNames = new HashMap<>();
	private Map<Integer, String> constraintNames = new HashMap<>();
	
	private String indent = "";
	
	private boolean printVariables = true;
	private boolean printObjectives = true;
	private boolean printConstraints = true;
	private boolean printAttributes = true;
	
	private boolean printSummaries = true;
	private boolean printPlaceholders = true;
	
	@Override
	public ISolutionPrinter<S> addObjectiveName(int index, String name) {
		objectiveNames.put(index, name);
		return this;
	}
		
	@Override
	public ISolutionPrinter<S> addVariableName(int index, String name) {
		variableNames.put(index, name);
		return this;
	}
	
	@Override
	public ISolutionPrinter<S> addConstraintName(int index, String name) {
		constraintNames.put(index, name);
		return this;
	}
	
	@Override
	public ISolutionPrinter<S> setObjectiveNames(Iterable<String> names) {
		int i = 0;
		for(String name : names)
			addObjectiveName(i++, name);
		return this;
	}
	
	@Override
	public ISolutionPrinter<S> setVariableNames(Iterable<String> names) {
		int i = 0;
		for(String name : names)
			addVariableName(i++, name);
		return this;
	}
	
	@Override
	public ISolutionPrinter<S> setConstraintNames(Iterable<String> names) {
		int i = 0;
		for(String name : names)
			addConstraintName(i++, name);
		return this;
	}
	
	@Override
	public boolean printVariables() {
		return printVariables;
	}
	
	@Override
	public boolean printObjectives() {
		return printObjectives;
	}
	
	@Override
	public boolean printConstraints() {
		return printConstraints;
	}
	
	@Override
	public boolean printAttributes() {
		return printAttributes;
	}
	
	@Override
	public boolean printSummaries() {
		return printSummaries;
	}
	
	public ISolutionPrinter<S> setPrintVariables(boolean printVariables) {
		this.printVariables = printVariables;
		return this;
	}

	public ISolutionPrinter<S> setPrintObjectives(boolean printObjectives) {
		this.printObjectives = printObjectives;
		return this;
	}

	public ISolutionPrinter<S> setPrintConstraints(boolean printConstraints) {
		this.printConstraints = printConstraints;
		return this;
	}

	public ISolutionPrinter<S> setPrintAttributes(boolean printAttributes) {
		this.printAttributes = printAttributes;
		return this;
	}
	
	public ISolutionPrinter<S> setPrintSummaries(boolean printSummaries) {
		this.printSummaries = printSummaries;
		return this;
	}

	@Override
	public String print(S solution) {
		if(solution == null)
			return "";
		
		resetIndent();
		String txt = "";
		
		if(printSummaries()) {
			setIndent(0);
			txt += printVariableSummary(solution);
			setIndent(2);
		}
		if(printVariables())
			txt += printVariables(solution);
		
		if(printSummaries()) {
			setIndent(0);
			txt += printAttributeSummary(solution);
			setIndent(2);
		}
		if(printAttributes())
			txt += printAttributes(solution);
		
		if(printSummaries()) {
			setIndent(0);
			txt += printObjectiveSummary(solution);
			setIndent(2);
		}
		if(printObjectives())
			txt += printObjectives(solution);
		
		if(printSummaries()) {
			setIndent(0);
			txt += printConstraintSummary(solution);
			setIndent(2);
		}
		if(printConstraints())
			txt += printConstraints(solution);
		
		return txt;
	}
	
	protected GenericSolutionPrinter<S> setIndent(String indent) {
		this.indent = indent;
		return this;
	}
	
	protected GenericSolutionPrinter<S> setIndent(int spaces) {
		return setIndent(StringUtils.repeat(' ', spaces));
	}
	
	protected GenericSolutionPrinter<S> increaseIndent(int spaces) {
		return setIndent(StringUtils.repeat(' ', spaces + getIndent().length()));
	}
	
	protected GenericSolutionPrinter<S> decreaseIndent(int spaces) {
		return setIndent(StringUtils.repeat(' ', getIndent().length() - spaces));
	}
	
	protected GenericSolutionPrinter<S> resetIndent() {
		return setIndent("");
	}
	
	protected String getIndent() {
		return indent;
	}
	
	protected String printObjectives(S solution) {
		String txt = "";
		for(int i = 0; i < solution.getNumberOfObjectives(); i++)
			txt += printObjective(i, solution.getObjective(i));
		return txt;
	}
	
	protected String printVariables(S solution) {
		String txt = "";
		for(int i = 0; i < solution.getNumberOfVariables(); i++)
			txt += printVariable(i, solution.getVariable(i));
		return txt;
	}
	
	protected String printAttributes(S solution) {
		String txt = "";
		for(Entry<String, Serializable> entry : solution.getAttributes().entrySet())
			txt += printAttribute(entry.getKey(), entry.getValue());
		return txt;
	}

	protected String printConstraints(S solution) {
		String txt = "";
		for(int i = 0; i < solution.getNumberOfConstraints(); i++)
			txt += printConstraint(i, solution.getConstraint(i));
		return txt;
	}
	
	protected String getNameOrIndex(int index, Map<Integer, String> map, String topic) {
		String name = map.get(index);
		if(name == null)
			return topic + "[" + String.valueOf(index) + "]";
		return name;
	}
	
	@Override
	public String getVariableName(int index) {
		return getNameOrIndex(index, variableNames, "Variable");
	}
	
	@Override
	public String getObjectiveName(int index) {
		return getNameOrIndex(index, objectiveNames, "Objective");
	}
	
	@Override
	public String getConstraintName(int index) {
		return getNameOrIndex(index, constraintNames, "Constraint");
	}
	
	@Override
	public String getVariableData(S solution, int index) {
		return getVariableData(solution.getVariable(index));
	}
	
	public String getVariableData(Variable variable) {
		return variable.toString();
	}
	
	@Override
	public String getConstraintData(S solution, int index) {
		return getConstraintData(solution.getConstraint(index));
	}
	
	public String getConstraintData(double constraint) {
		return "" + constraint;
	}
	
	@Override
	public String getObjectiveData(S solution, int index) {
		return getObjectiveData(solution.getObjective(index));
	}
	
	public String getObjectiveData(double objective) {
		return "" + objective;
	}
	
	protected String printObject(Object value)  {
		if(value == null)
			return "<null>";
		if(value instanceof Serializable)
			return printSerializable((Serializable) value);
		return value.toString();
	}
	
	protected String printSerializable(Serializable value) {
		if(value instanceof boolean[])
			return Arrays.toString((boolean[])value);
		if(value instanceof byte[])
			return Arrays.toString((byte[])value);
		if(value instanceof char[])
			return Arrays.toString((char[])value);
		if(value instanceof double[])
			return Arrays.toString((double[])value);
		if(value instanceof float[])
			return Arrays.toString((float[])value);
		if(value instanceof int[])
			return Arrays.toString((int[])value);
		if(value instanceof long[])
			return Arrays.toString((long[])value);
		if(value instanceof Object[])
			return Arrays.toString((Object[])value);
		if(value instanceof Map.Entry<?, ?>)
			return printObject(((Map.Entry<?, ?>) value).getKey()) + " = " + printObject(((Map.Entry<?, ?>) value).getValue()); 
		return value + "";
	}
	
	protected String printAttribute(String key, Serializable value) {
		return getIndent() + printObject(key) + ": " + printObject(value) + "\n";
	}
	
	protected String printVariable(int index, Variable variable) {
		if(!printPlaceholders() && variable instanceof IPlaceholderVariable)
			return "";
		return getIndent() + getVariableName(index) + ": " + getVariableData(variable) + "\n";
	}
	
	protected String printObjective(int index, double objective) {
		return getIndent() + getObjectiveName(index) + ": " + getObjectiveData(objective) + "\n";
	}
	
	protected String printConstraint(int index, double constraint) {
		return getIndent() + getConstraintName(index) + ": " + getConstraintData(constraint) + "\n";
	}
	
	protected String printVariableSummary(S solution) {
		String info = printPlaceholders() ? "" : " (placeholders are skipped)";
		return getIndent() + "Number of variables: " + solution.getNumberOfVariables() + info + "\n";
	}
	
	protected String printObjectiveSummary(S solution) {
		return getIndent() + "Number of objectives: " + solution.getNumberOfObjectives() + "\n";
	}
	
	protected String printConstraintSummary(S solution) {
		return getIndent() + "Number of constraints: " + solution.getNumberOfConstraints() + "\n";
	}
	
	protected String printAttributeSummary(S solution) {
		return getIndent() + "Number of attributes: " + solution.getAttributes().size() + "\n";
	}

	@Override
	public boolean printPlaceholders() {
		return this.printPlaceholders;
	}
	
	@Override
	public ISolutionPrinter<S> setPrintPlaceholders(boolean printPlaceholders) {
		this.printPlaceholders = printPlaceholders;
		return this;
	}
}
