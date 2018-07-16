Lei Zhang

zhangl64@mcmaster.ca

1. Please read LICENSE file for the copyright. 

2. This project is one of my PhD projects. This is a J2EE version of TPC-W based on the original version from University of Virginia.

3. The original TPC-W only implemented a closed network, where the number of EBs is fixed. I extended it to open and semi-open networks.

4. In open networks, the number of EBs varies, and the simulated users access to the server following a Possion process. After one request, users leave the server.

5. In semi-open networks, simulated users could send multiple web interactions to the server, and then leave the server.

6. Semi-open networks are more complex to analyze, however, it is more close to the real user behaviors on e-commerce websites.

7. This repository consists of the following branches: 
  
  1. tpcw-server-closed and tpcw-client-closed: the source code for closed networks. It is the original version from University of Virgina.
  
  2. tpcw-server-open: the source code of J2EE server for open and semi-open networks.
  
  3. tpcw-client-open: the source code of J2EE client for open networks.
  
  4. tpcw-client-semi: the source code of J2EE client for semi-open networks.

If you use this TPC-W as part of your academic publication, we request that you cite our work as follows:

```@article{zhang2018apem,
	title={{APEM} --- Approximate Performance Evaluation for Multi-Core Computers},
	author={Zhang, Lei and Down, Douglas G},
	journal={Journal of Circuits, Systems and Computers},
	year={2018},
	publisher={World Scientific}	
}
```
