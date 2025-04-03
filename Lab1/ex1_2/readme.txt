c) Analyze the results accordingly. Which classes/methods offer less coverage? Are all possible [decision]
   branches being covered?
   Collect evidence of the coverage for “BoundedSetOfNaturals”.

   Ao analisar o Jacoco, a classe apresenta uma cobertura de apenas 54% de instruções e 50% de ramos.

   BoundedSetOfNaturals.java.html mostra também que métodos como intersects() e partes do método add(int element) não
   são totalmente cobertos



e) Run Jacoco coverage analysis and compare with previous results. In particular, compare the “before”
   and “after” coverage for the BoundedSetOfNaturals class.

A análise da cobertura permitiu perceber que a classe BoundedSetOfNaturals inicialmente tinha
 baixa cobertura (54% instruções, 50% ramos).
 Após adicionar testes direcionados para esses casos, a cobertura subiu para 86% instruções e 85% ramos, o que
 indica uma melhoria na qualidade dos testes.