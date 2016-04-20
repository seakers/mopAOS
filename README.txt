README 
mopAOS is an Java-based open source tool to develop and test multiobjective adaptive operator selection (AOS). mopAOS is built upon MOEAFramework to use existing multiobjective evolutionary algorithms (MOEA) and standard multiobjective benchmarking problems. 

The two main components of an AOS strategy are the credit assignment strategy that defines how to reward an operator based on its impact in the search process and the operator selection strategy that uses the rewards to determine the next operator to apply. mopAOS includes several credit assignment strategies for multiobjective AOS. It also includes several operator selection strategies that can be used for both single-objective and multiobjective problems

The main experiment class AOSCreditTest contains the experimental setup used in a paper submission (Hitomi, N., Selva, D. "A Classification and Comparison of Credit Assignment Strategies in Multiobjective Adaptive Operator Selection". IEEE Transactions in Evolutionary Computation 2016). See paper for details on the methods.


All parameter values used in our experiments are set as default values. These parameters include operator parameters, number of weight vectors, population size, and operator selector parameters.

