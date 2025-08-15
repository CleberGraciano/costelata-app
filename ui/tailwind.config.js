/** @type {import('tailwindcss').Config} */
const plugin = require('tailwindcss/plugin');

module.exports = {
  content: [
    "./src/**/*.{html,ts}",
    "./node_modules/primeng/**/*.{js,ts}"
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primary: {
          50:  '#fff9f3',
          100: '#fef0db',
          200: '#fdcf85',
          300: '#f3b061',
          400: '#d57b57',
          500: '#b24f44', // Principal (usado como var(--color-primary))
          600: '#83332e',
          700: '#662926',
          800: '#4e211f',
          900: '#402020'
        },
        error: '#dc2626', // Defina 'error' também para padronizar o uso com PrimeNG
      },
      fontFamily: {
        heading: ['"Big Shoulders Display"', 'sans-serif'],
        display: ['"Big Shoulders Display"', 'sans-serif'],
        body: ['Inter', 'sans-serif']
      }
    }
  },
  plugins: [
    plugin(function({ addBase, theme }) {
      addBase({
        ':root': {
          '--color-50': theme('colors.primary.50'),
          '--color-100': theme('colors.primary.100'),
          '--color-200': theme('colors.primary.200'),
          '--color-300': theme('colors.primary.300'),
          '--color-400': theme('colors.primary.400'),
          '--color-500': theme('colors.primary.500'),
          '--color-600': theme('colors.primary.600'),
          '--color-700': theme('colors.primary.700'),
          '--color-800': theme('colors.primary.800'),
          '--color-900': theme('colors.primary.900'),

          '--color-primary': theme('colors.primary.500'),
          '--color-primary-hover': theme('colors.primary.600'),
          '--color-primary-focus': theme('colors.primary.200'),
          '--color-primary-bg': theme('colors.primary.50'),

          '--color-dark': theme('colors.primary.900'),
          '--color-light': '#f9fafb',
          '--color-error': theme('colors.error'),
          '--color-success': '#16a34a',
          '--color-info': '#2563eb'
        },
        'body': {
          fontFamily: theme('fontFamily.body'),
          backgroundColor: 'var(--color-light)',
          color: theme('colors.gray.800')
        },
        'h1, h2, h3, h4, h5, h6': {
          fontFamily: theme('fontFamily.heading'),
        }
      });
    })
  ]
}
