const re = /\.\/(.*)\.svg/;
const req = require.context('../../icons/svg', false, /\.svg$/);
const requireAll = requireContext => requireContext.keys();

const svgIcons = requireAll(req).map(i => {
  return i.match(re)[1];
});

export default svgIcons;
