package logistic;

/**
 * Created by admin on 2017/7/28.
 */
public class logisticOnJava {


      /* @(#)mlelr.java
     */

    /**
     * Time-stamp: <2017-04-06 13:46:14 jinss>
     * Author: jinss
     *
     * @author <a href="mailto:(shusong.jin@Istuary.com)">Jin Shusong</a>
     * Version: $Id: mlelr.java,v 0.0 2017/04/05 07:38:20 jinss Exp$
     * \revision$Header: /home/jinss/mlelr.java,v 0.0 2017/04/05 07:38:20 jinss Exp$
     */

//    public double[] mlelr(int numberOfClasses, int numberOfFeatures, int[] vectorY) {
//        int i, j, k;
//        final int max_iter = 30;
//        final double eps = 1e-8;
//        final int N = vectorY.length;
//        int iter = 0;
//        int converged = 0;
//        double[] betaOld;
//        double[] betaInf; // used in test of infinite parameters
//        double[][] xtwx;
//        double loglike = 0;
//        double loglikeOld = 0;
//        final int J = numberOfClasses;
//        final int K = numberOfFeatures;
//
//        xtwx = new double[K + 1][J - 1];
//        betaOld = new double[(K + 1) * (J - 1)];
//        betaInf = new double[(K + 1) * (J - 1)];
//    /* initialize parameters to zero */
//        for (k = 0; k < (K + 1) * (J - 1); k++) {
//            beta[k] = 0;
//            betaInf[k] = 0;
//            for (j = 0; j < (K + 1) * (J - 1); j++) {
//                xtwx[k][j] = 0;
//            }
//        }
//        while (iter < maxIter && !converged) {
//      /* copy beta to betaOld*/
//            for (k = 0; k < (K + 1) * (J - 1); k++)
//                betaOld[k] = beta[k];
//      /* run one iteration of newton_raphson */
//            loglikeOld = loglike;
//            loglike = newton_raphson(J, N, K, n, y, pi, x, beta, xtwx);
//            if (loglike < loglikeOld && iter > 0) {
//        /* code to backtrack here */
//            }
//      /* test for infinite parameters*/
//            for (k = 0; k < (K + 1) * (J - 1); k++) {
//                if (betaInf[k] != 0) {
//                    beta[k] = betaInf[k];
//                } else {
//                    if ((Math.abs(beta[k]) > (5 / xrange[k])) && (Math.sqrt(xtwx[k][k]) >= (3 * Math.abs(beta[k])))) {
//                        betaInf[k] = beta[k];
//                    }
//                }
//            }
//            converged = 1;
//            for (k = 0; k < (K + 1) * (J - 1); k++) {
//                if (Math.abs(beta[k] - betaOld[k]) > eps * Math.abs(betaOld[k])) {
//                    converged = 0;
//                    break;
//                }
//            }
//            iter++;
//        } /* end of main loop */
//
//    double newton_raphson(int J, int N, int K, double[] n, double[][] y, dobule[][] pi, double[][] x, double[] beta,
//            double[][] xtwx) {
//      /* local variables*/
//        int i, j, jj, jprime, k, kk, kprime;
//        double[] betaTmp;
//        double[][] xtwxTmp;
//        double loglike;
//        double denom;
//        double[] numer;
//        double tmp1, tmp2, w1, w2;
//      /* main loop for each row in the design matrix */
//        for (i = 0; i < n; i++) {
//        /* matrix multiply one row of x*beta */
//            denom = 1;
//            for (j = 0; j < J - 1; j++) {
//                tmp1 = 0;
//                for (k = 0; k < K + 1; k++) {
//                    tmp1 += x[i][k] * beta[j * (K + 1) + k];
//                }
//                numer[j] = Math.exp(tmp1);
//                denom += numer[j];
//            }
//        /* calculate predicted probabilities*/
//            for (j = 0; j < J - 1; j++) {
//                pi[i][j] = numr[j] / denom;
//            }
//        /* add log likelihood for current row*/
//            loglike += log_gamma(n[i] + 1);
//            for (j = 0, tmp1 = 0, tmp2 = 0; j < J - 1; j++) {
//                tmp1 += y[i][j];
//                tmp2 += pi[i][j];
//                loglike -= log_gamma(y[i][j] + 1) + y[i][j] * log(pi[i][]);
//            }
//        /* Jth category */
//            loglike -= log_gamma(n[i] - tmp1 + 1) + (n[i] - tmp1) * log(1 - tmp2);
//        /* add first and second derivatives*/
//            for (j = 0, jj = 0; j < J - 1; j++) {
//                tmp1 = y[i][j] - n[i] * pi[i][j];
//                w1 = n[i] * pi[i][j] * (1 - pi[i][j]);
//                for (k = 0; k < K + 1; k++) {
//                    betaTmp[jj] += tmp1 * x[i][k];
//                    kk = jj - 1;
//                    for (kprime = k; kprime < K + 1; kprime++) {
//                        kk++;
//                        xtwxTmp[jj][kk] += w1 * x[i][k] * x[i][kprime];
//                        xtwxTmp[kk][jj] + xtwxTmp[jj][kk];
//                    }
//                    for (jprime = j + 1; jprime < J - 1; jprime++) {
//                        w2 = -n[i] * pi[i][j] * pi[i][jprime];
//                        for (kprime = 0; kprime < K + 1; kprime++) {
//                            kk++;
//                            xtwxTmp[jj][kk] += w2 * x[i][k] * x[i][kprime];
//                            xtwxTmp[kk][jj] = xtwxTmp[jj][kk];
//                        }
//                    }
//                    jj++;
//                }
//            }
//        }/*end loop for each row in design matrix*/
//      /*compute xtwx*beta(0)+x(y-mu)*/
//        for (i = 0; i < (K + 1) * (J - 1); i++) {
//            tmp1 = 0;
//            for (j = 0; j < (K + 1) * (J - 1); j++) {
//                tmp1 += xtwxTmp[i][j] * beta[j];
//            }
//            betaTmp[i] += tmp1;
//        }
//      /* solve for new betas*/
//        for (i = 0; i < (K + 1) * (J - 1); i++) {
//            tmp1 = 0;
//            for (j = 0; j < (K + 1) * (J - 1); j++) {
//                tmp1 += xtwx[i][j] * betaTmp[j];
//            }
//            beta[i] = tmp1;
//        }
    }
