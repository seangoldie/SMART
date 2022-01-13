# SMART
 Synthesized Musical Algorithms in Real Time - a generative music program written in Java by Sean Goldie

 This experimental generative music application was completed as the final project for Algorithmic Composition and Computer Music Programming in Java at NYU, Fall 2021.

 SMART is an algorithmic music program that generates triadic chords using a three-oscillator sawtooth synthesizer and features a single user-controlled parameter that alters the generative process. The novel parameter acts as a scalar applied to the semi-randomly generated values, scaling up their bounded limits to a maximum of 200% by default. This user interactivity allows for easy, real-time exploration of microtonality and inharmonicity in a responsive, powerful, and intuitive system. The program was written in pure Java, using the JMSL and JSyn computer music APIs for scheduling and synthesis, respectively.