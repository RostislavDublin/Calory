import {CrudListConfig} from './crud-list-config';

export class CrudListConfigDefault extends CrudListConfig {

  constructor() {
    super(
      'CRUD List',
      [
        {id: 'id', name: 'Id', filter: true},
        {id: 'code', name: 'Code'},
        {id: 'name', name: 'Name', filter: true}
      ],
      [
        {id: 1, code: 'Α α', name: 'alpha, άλφα'},
        {id: 2, code: 'Β β', name: 'beta, βήτα'},
        {id: 3, code: 'Γ γ', name: 'gamma, γάμμα'},
        {id: 4, code: 'Δ δ', name: 'delta, δέλτα'},
        {id: 5, code: 'Ε ε', name: 'epsilon, έψιλον'},
        {id: 6, code: 'Ζ ζ', name: 'zeta, ζήτα'},
        {id: 7, code: 'Η η', name: 'eta, ήτα'},
        {id: 8, code: 'Θ θ', name: 'theta, θήτα'},
        {id: 9, code: 'Ι ι', name: 'iota, ιώτα'},
        {id: 10, code: 'Κ κ', name: 'kappa, κάππα'},
        {id: 11, code: 'Λ λ', name: 'la(m)bda, λά(μ)βδα'},
        {id: 12, code: 'Μ μ', name: 'mu, μυ'},
        {id: 13, code: 'Ν ν', name: 'nu, νυ'},
        {id: 14, code: 'Ξ ξ', name: 'xi, ξι'},
        {id: 15, code: 'Ο ο', name: 'omicron, όμικρον'},
        {id: 16, code: 'Π π', name: 'pi, πι'},
        {id: 17, code: 'Ρ ρ', name: 'rho, ρώ'},
        {id: 18, code: 'Σ σ/ς', name: 'sigma, σίγμα'},
        {id: 19, code: 'Τ τ', name: 'tau, ταυ'},
        {id: 20, code: 'Υ υ', name: 'upsilon, ύψιλον'},
        {id: 21, code: 'Φ φ', name: 'phi, φι'},
        {id: 22, code: 'Χ χ', name: 'chi, χι'},
        {id: 23, code: 'Ψ ψ', name: 'psi, ψι'},
        {id: 24, code: 'Ω ω', name: 'omega, ωμέγα'},
      ]);
  }
}
