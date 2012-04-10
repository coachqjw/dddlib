package com.dayatang.rule;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.rules.StatelessRuleSession;

import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.rule.examples.Person;

public class StatelessRuleTemplateTest {
	
	private StatelessRuleTemplate instance;
	private String ruleDrl = "/Person.drl";
	private Person chencao;
	private Person xishi;
	private Person yyang;

	@Before
	public void setUp() throws Exception {
		instance = createStatelessRuleTemplate();
		chencao = new Person(1L, "chencao", "male");
		xishi = new Person(2L, "xishi", "female");
		yyang = new Person(3L, "yyang", "male");
	}

	private StatelessRuleTemplate createStatelessRuleTemplate() {
		InputStream ruleSource = getClass().getResourceAsStream(ruleDrl);
		StatelessRuleTemplate result = new StatelessRuleTemplate(new RuleServiceProviderImpl(), null, ruleSource, null, null);
		return result;
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testExecute() throws Exception {
		final List objects = Arrays.asList(chencao, xishi, yyang);
		List results = instance.execute(new StatelessRuleCallback() {
			
			@Override
			public List doInRuleSession(StatelessRuleSession session) throws Exception {
				return session.executeRules(objects);
			}
		});

		// Validate
		assertTrue(results.containsAll(objects));
		assertEquals(60, chencao.getRetireAge());
		assertEquals(55, xishi.getRetireAge());
		assertEquals(60, yyang.getRetireAge());
		
	}

}
